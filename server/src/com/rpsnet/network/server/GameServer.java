package com.rpsnet.network.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.rpsnet.network.*;

import java.io.IOException;
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

        //Initialise remoteClients and state count
        remoteClients = new HashMap<>();
        playerCount = new HashMap<>();
        refreshPlayerCount();

        //Start update thread
        updateThread = new UpdateThread();
        updateThread.start();
    }

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

    public void removeClient(Connection connection)
    {
        if(remoteClients.containsKey(connection))
        {
            remoteClients.remove(connection);
        }
        else
        {
            Log.error("Client with connection ID: " + connection.getID() + " is not in client list!");
        }
    }

    public void registerName(Connection connection, String name)
    {
        if(remoteClients.containsKey(connection))
        {
            remoteClients.get(connection).setName(name);
            Log.info("Client " + connection.getID() + " has registered name: " + name);
        }
        else
        {
            Log.error("Tried to set a name for a client that does not exist! ID: " + connection.getID());
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
}
