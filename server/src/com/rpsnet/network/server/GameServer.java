package com.rpsnet.network.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.rpsnet.network.NetworkHandler;
import com.rpsnet.network.Packets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GameServer
{
    Server server;

    private Map<Connection, RemoteClient> remoteClients;

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

        //Initialise remoteClients
        remoteClients = new HashMap<>();
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

    public int playerCount()
    {
        return remoteClients.size();
    }

    public int playerCountInState(ClientState state)
    {
        int count = 0;

        for (RemoteClient r : remoteClients.values())
        {
            if(r.getClientState() == state)
            {
                count++;
            }
        }

        return count;
    }

    public static void main(String[] args) throws IOException {
        new GameServer();
    }
}
