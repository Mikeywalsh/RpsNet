package com.rpsnet.game;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.minlog.Log;
import com.rpsnet.network.NetworkHandler;
import com.rpsnet.network.Packets;

import java.io.IOException;
import java.util.Scanner;

public class GameClient extends Thread
{
    Client client;
    String name;

    private Connection serverConnection;

    private Packets.PlayerCount playerCountInfo;

    public void run()
    {
        //Prompt the user for their name before connecting them
        System.out.println("Please enter your name...");
        Scanner scanner = new Scanner(System.in);
        name = scanner.nextLine();
        System.out.println("Hello " + name + "! Connecting to server...\n");

        //Create the client and start running it in another thread
        client = new Client();
        new Thread(client).start();

        //Register classes with Kryonet
        NetworkHandler.register(client);

        //Add the event listener to the client thread
        client.addListener(new ClientListener(this));

        //Attempt to connect to the server
        try
        {
            client.connect(5000, "127.0.0.1", NetworkHandler.port);
        }
        catch (IOException ex)
        {
            System.out.println("Could not find server...");
        }
    }

    public void requestPlayerCount()
    {
        if(serverConnection != null)
        {
            serverConnection.sendTCP(new Packets.RequestPlayerCount());
        }
        else
        {
            Log.error("Tried to request player count when there was no server connection!");
        }
    }

    public Packets.PlayerCount getPlayerCountInfo() { return playerCountInfo; }

    public String getPlayerName(){ return name; }

    public void setServerConnection(Connection con) { serverConnection = con; }

    public void setPlayerCountInfo(Packets.PlayerCount info) { playerCountInfo = info; }

    public boolean connected()
    {
        if(client == null)
        {
            return false;
        }

        return client.isConnected();
    }
}
