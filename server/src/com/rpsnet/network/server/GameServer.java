package com.rpsnet.network.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.rpsnet.network.NetworkHandler;

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
            Log.debug("Client with connection ID: " + connection.getID() + " is already connected!");
        }
    }

    public static void main(String[] args) throws IOException {
        new GameServer();
    }
}
