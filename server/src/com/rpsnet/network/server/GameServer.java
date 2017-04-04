package com.rpsnet.network.server;

import com.esotericsoftware.kryonet.Server;
import com.rpsnet.network.NetworkHandler;

import java.io.IOException;

public class GameServer
{
    Server server;

    public GameServer() throws IOException {
        //Create the server object
        server = new Server(){};
        System.out.println("Server started...");

        //Register classes with Kryonet
        NetworkHandler.register(server);

        //Add a listener to the server and bind the port
        server.addListener(new ServerListener());
        server.bind(NetworkHandler.port);

        //Start running the server
        server.start();
    }

    public static void main(String[] args) throws IOException {
        new GameServer();
    }
}
