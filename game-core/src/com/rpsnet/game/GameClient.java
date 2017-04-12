package com.rpsnet.game;

import com.badlogic.gdx.Screen;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.minlog.Log;
import com.rpsnet.game.screens.GameScreen;
import com.rpsnet.game.screens.MainMenuScreen;
import com.rpsnet.game.screens.NetScreen;
import com.rpsnet.network.GameChoice;
import com.rpsnet.network.NetworkHandler;
import com.rpsnet.network.Packets;

import java.io.IOException;

public class GameClient
{
    private Client client;
    private NetScreen currentScreen;

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
                    displayErrorMessage("Could not find server...");
                }
            }
        }.start();
    }

    /**
     * Used to disconnect from the server manually
     * @param reason The reason for disconnecting
     */
    public void abortConnection(String reason)
    {
        if(client.isConnected())
        {
            client.stop();
        }

        displayErrorMessage(reason);
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

    /**
     * Used to move to the game screen when the server has found a match for a player
     * @param setupInfo Information about the match from the server
     */
    public void startGame(Packets.GameSetup setupInfo)
    {
        ((MainMenuScreen)currentScreen).getGame().setGameInfo(setupInfo);
    }

    public void makeChoice(GameChoice choice)
    {
        if(!(currentScreen instanceof GameScreen))
            return;

        //Prepare the packet to send to the server
        Packets.ChoiceMade choiceMade = new Packets.ChoiceMade();
        choiceMade.gameID = ((GameScreen)currentScreen).getGameID();
        choiceMade.choice = choice;

        //Send the packet to the server
        serverConnection.sendTCP(choiceMade);
    }

    public Packets.PlayerCount getPlayerCountInfo() { return playerCountInfo; }

    public String getPlayerName(){ return name; }

    public void setCurrentScreen(NetScreen screen)
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

        currentScreen.updateConnectionInfo(connected);
    }

    public Screen getCurrentScreen()
    {
        return currentScreen;
    }

    public void displayErrorMessage(String message)
    {
        currentScreen.displayErrorMessage(message);
    }
}
