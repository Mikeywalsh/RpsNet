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

/**
 * This class is used as a bridge between Kryonet and Libgdx
 * Allows the server to influence what appears onscreen
 * Allows the client to send data to the server when required
 */
public class GameClient
{
	/**
	 * The Kryonet client connection for this GameClient
	 */
    private Client client;

	/**
	 * The current game screen that is active
	 */
	private NetScreen currentScreen;

	/**
	 * The name of this client
	 */
    private String name;

	/**
	 * The connection to the server
	 * Can be null
	 */
	private Connection serverConnection;

	/**
	 * Information about the amount of other players connected to the server
	 */
    private Packets.PlayerCount playerCountInfo;

    /**
     * Creates a new GameClient and registers it with Kryonet
     * Also attaches a ClientListener to it
     */
    public GameClient()
    {
        //Create the client and start running it in another thread
        client = new Client();

        //Register classes with Kryonet
        NetworkHandler.register(client);

        //Add the event listener to the client thread
        client.addListener(new ClientListener(this));
    }

    /**
     * Attempts to connect to a server with the given name and IP address
     * @param inputIP The user input IP
     * @param inputName The user input name
     */
    public void attemptConnection(String inputIP, String inputName)
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
                    client.connect(5000, inputIP, NetworkHandler.port);
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

	/**
	 * Used to make a choice in the current game
	 * @param choice The choice to make
	 */
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

	/**
	 * Gets the current player count information
	 * @return The current player count information
	 */
	public Packets.PlayerCount getPlayerCountInfo() { return playerCountInfo; }

	/**
	 * Gets the name of this client
	 * @return The name of this client
	 */
    public String getPlayerName(){ return name; }

	/**
	 * Gets the current screen
	 * @return The current screen
	 */
	public Screen getCurrentScreen()
	{
		return currentScreen;
	}

	/**
	 * Sets the current screen to the passed in screen reference
	 * @param screen The screen to set the current screen to
	 */
    public void setCurrentScreen(NetScreen screen)
    {
        currentScreen = screen;
    }

	/**
	 * Sets the server connection to the passed in reference
	 * @param con The server set the connection of
	 */
	public void setServerConnection(Connection con)
    {
        serverConnection = con;
        updateCurrentScreen();
    }

	/**
	 * Sets the current player count info
	 * Called when the server has updated the client about the current player count
	 * @param info The current player count info
	 */
	public void setPlayerCountInfo(Packets.PlayerCount info)
    {
        playerCountInfo = info;
        updateCurrentScreen();
    }

	/**
	 * Updates the current game screen with information about the server connection and any errors
	 */
	public void updateCurrentScreen()
    {
        Boolean connected = client.isConnected();

        currentScreen.updateConnectionInfo(connected);
    }

	/**
	 * Displays an error message to the current screen
	 * @param message The error message to display
	 */
	public void displayErrorMessage(String message)
    {
        currentScreen.displayErrorMessage(message);
    }
}
