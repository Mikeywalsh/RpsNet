package com.rpsnet.network.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.rpsnet.network.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameServer
{
    /**
     * Kryonet server object
     */
    Server server;

    /**
     * Seperate thread that handles updating of server-side data
     */
    UpdateThread updateThread;

    /**
     * Contains a list of active connections and their respective client objects
     */
    private Map<Connection, RemoteClient> remoteClients;

    /**
     * Contains the amount of players in each game state, so that it does not have to be recalculated for every request
     */
    private Map<ClientState, Integer> playerCount;

    /**
     * The amount of games created since the server was launched
     * Used to create unique game ID's
     */
    private int gamesCreated;

    public GameServer() throws IOException {
        //Create the server object
        server = new Server();
        Log.debug("Server started...");

        //Register classes with Kryonet
        NetworkHandler.register(server);

        //Add a listener to the server and bind the port
        server.addListener(new ServerListener(this));
        server.bind(NetworkHandler.port);

        //Start running the server
        server.start();

        //Initialise remoteClients, state count and other variables
        remoteClients = new HashMap<>();
        playerCount = new HashMap<>();
        refreshPlayerCount();
        gamesCreated = 0;

        //Start update thread
        updateThread = new UpdateThread();
        updateThread.gameServer = this;
        updateThread.run();
    }

    /**
     * Adds a new client to the hashmap of clients
     * @param connection The connection for the new client
     */
    public void addClient(Connection connection)
    {
        if(!remoteClients.containsKey(connection))
        {
            remoteClients.put(connection, new RemoteClient(connection));
        }
        else
        {
            Log.info("Client with connection ID: " + connection.getID() + " is already connected!");
        }
    }

    /**
     * Removed a client from the hashmap of clients
     * @param connection The connection for the client being removed
     */
    public void removeClient(Connection connection)
    {
        if(remoteClients.containsKey(connection))
        {
            remoteClients.remove(connection);
        }
        else
        {
            Log.error("Connection with ID: " + connection.getID() + " is not in client list!");
        }
    }

    public void registerName(Connection connection, String name)
    {
        if(remoteClients.containsKey(connection))
        {
            //Set the initial value of the response to be accepted
            Packets.RegisterNameResponse.ResponseType responseType = Packets.RegisterNameResponse.ResponseType.ACCEPTED;

            //Check if the client already has a name
            if(remoteClients.get(connection).getClientState() != ClientState.NAMELESS)
            {
                responseType = Packets.RegisterNameResponse.ResponseType.ALREADY_HAS_NAME;
            }
            else
            {
                //Check if the requested name exists in the list of remote clients
                for (RemoteClient client : remoteClients.values())
                {
                    if (name.equals(client.getPlayerName()))
                    {
                        responseType = Packets.RegisterNameResponse.ResponseType.NAME_EXISTS;
                    }
                }
            }

            //If the response is still accepted, then set the clients name
            if(responseType == Packets.RegisterNameResponse.ResponseType.ACCEPTED)
            {
                remoteClients.get(connection).setName(name);
                Log.info("Client " + connection.getID() + " has registered name: " + name);
            }

            //Create a response for the client
            Packets.RegisterNameResponse response = new Packets.RegisterNameResponse();
            response.requestedName = name;
            response.responseType = responseType;

            //Now send the response to the client
            connection.sendTCP(response);
        }
        else
        {
            Log.error("A remote client with the provided connection does not exist! ID: " + connection.getID());
        }
    }

    public void queueClientMatchmaking(Connection connection)
    {
        if(remoteClients.containsKey(connection))
        {
            RemoteClient client = remoteClients.get(connection);

            //Only queue the client for matchmaking if they are idle
            if(client.getClientState() == ClientState.IDLE)
            {
                client.setClientState(ClientState.QUEUED);
                Log.info(remoteClients.get(connection).getPlayerName() + " has queued for matchmaking!");
            }
            else
            {
                Log.error("Tried to set state to QUEUED for client " + client.getPlayerName() + " with state: " + client.getClientState());
            }
        }
        else
        {
            Log.error("A remote client with the provided connection does not exist! ID: " + connection.getID());
        }
    }

    /**
     * Called every few seconds to check if any matches can be created with the queued players
     */
    public void attemptMatchmake()
    {
        //Return if there aren't enough players on the server
        if(remoteClients.size() < 2)
            return;

        ArrayList<RemoteClient> players = new ArrayList<>();

        //Try and pair queued players with eachother
        for(RemoteClient client : remoteClients.values())
        {
            if(client.getClientState() == ClientState.QUEUED)
            {
                players.add(client);
                if(players.size() == 2)
                {
                    //Create a GameSetup packet to send to each client
                    Packets.GameSetup gameSetup = new Packets.GameSetup();
                    gameSetup.gameID = gamesCreated;

                    //Send the packet to the first player
                    gameSetup.opponentName = players.get(1).getPlayerName();
                    players.get(0).getConnection().sendTCP(gameSetup);

                    //Send the packet to the second player
                    gameSetup.opponentName = players.get(0).getPlayerName();
                    players.get(1).getConnection().sendTCP(gameSetup);

                    //Change the state for each player to be ingame
                    players.get(0).setClientState(ClientState.INGAME);
                    players.get(1).setClientState(ClientState.INGAME);

                    //Output matchup to log
                    Log.info("Game " + gamesCreated + ": " + players.get(0).getPlayerName() + " vs " + players.get(1).getPlayerName());
                    gamesCreated++;

                    //Clear the players list and carry on running, so multiple games can be created each function call
                    players = new ArrayList<>();
                }
            }
        }

    }

    public Map<ClientState, Integer> getPlayerCount()
    {
        return playerCount;
    }

    public void refreshPlayerCount()
    {
        Map<ClientState, Integer> newPlayerCount = new HashMap<>();

        //Set the value for each state to be zero
        for(ClientState state : ClientState.values())
        {
            newPlayerCount.put(state, 0);
        }

        //Now get the amount of players in each state
        for (RemoteClient r : remoteClients.values())
        {
            newPlayerCount.put(r.getClientState(), newPlayerCount.get(r.getClientState()) + 1);
        }

        //Now assign the new count map to the old count map
        playerCount = newPlayerCount;
    }

    public void broadcastPlayerCount()
    {
        Packets.PlayerCount playerCount = new Packets.PlayerCount();
        playerCount.playerCount = getPlayerCount();

        for(Connection connection : remoteClients.keySet())
        {
            connection.sendTCP(playerCount);
        }
    }
}
