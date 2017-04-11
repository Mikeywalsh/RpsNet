package com.rpsnet.game;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.minlog.Log;
import com.rpsnet.network.NetworkHandler;
import com.rpsnet.network.Packets;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.IOException;
import java.util.Scanner;

public class GameClient extends Thread
{
    private Client client;
    private String name;
    private Boolean attemptingConnection = false;

    private Connection serverConnection;

    private Packets.PlayerCount playerCountInfo;

    public void attemptConnection(String inputName)
    {
        //Set the name of the client
        name = inputName;

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
            attemptingConnection = true;
            client.connect(5000, "192.168.1.79", NetworkHandler.port);
        }
        catch (IOException ex)
        {
            attemptingConnection = false;
            System.out.println("Could not find server...");
        }
    }

    public void requestPlayerCount()
    {
        if(serverConnection != null)
        {
            serverConnection.sendTCP(new Packets.PlayerCountRequest());
        }
        else
        {
            Log.error("Tried to contact server when there was no server connection!");
        }
    }

    public void requestMatchmake()
    {
        if(serverConnection != null)
        {
            serverConnection.sendTCP(new Packets.MatchmakeRequest());
        }
        else
        {
            Log.error("Tried to contact server when there was no server connection!");
        }
    }

    public Boolean isAttemptingConnection()
    {
        return attemptingConnection;
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
