package com.rpsnet.game;

import com.badlogic.gdx.Screen;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.minlog.Log;
import com.rpsnet.game.actors.MainMenuActors;
import com.rpsnet.game.screens.MainMenuScreen;
import com.rpsnet.network.NetworkHandler;
import com.rpsnet.network.Packets;

import java.io.IOException;

public class GameClient
{
    private Client client;
    private Screen currentScreen;

    private String name;

    private Connection serverConnection;

    private Packets.PlayerCount playerCountInfo;

    public GameClient()
    {
        //Create the client and start running it in another thread
        client = new Client();

        //Register classes with Kryonet
        NetworkHandler.register(client);

        //Add the event listener to the client thread
        client.addListener(new ClientListener(this));
    }

    public void attemptConnection(String inputName)
    {
        new Thread("Connect")
        {
            public void run()
            {
                //Attempt to connect to the server
                try
                {
                    client.start();
                    name = inputName;
                    client.connect(5000, "127.0.0.1", NetworkHandler.port);
                }
                catch (IOException ex)
                {
                    updateCurrentScreen();
                    System.out.println("Could not find server...");
                }
            }
        }.start();
    }

    /**
     * Used to disconnect from the server manually
     */
    public void abortConnection()
    {
        if(client.isConnected())
        {
            client.stop();
        }

        updateCurrentScreen();
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

    public Packets.PlayerCount getPlayerCountInfo() { return playerCountInfo; }

    public String getPlayerName(){ return name; }

    public void setCurrentScreen(Screen screen)
    {
        currentScreen = screen;
    }

    public void setServerConnection(Connection con)
    {
        serverConnection = con;
        updateCurrentScreen();
    }

    public void setPlayerCountInfo(Packets.PlayerCount info)
    {
        playerCountInfo = info;
        updateCurrentScreen();
    }

    public void updateCurrentScreen()
    {
        Boolean connected = client.isConnected();

        if(currentScreen instanceof MainMenuScreen)
        {
            ((MainMenuScreen)currentScreen).updateConnectionInfo(connected);
        }
    }
}
