package com.rpsnet.game;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.rpsnet.game.screens.GameScreen;
import com.rpsnet.game.screens.MainMenuScreen;
import com.rpsnet.network.Packets;

public class ClientListener extends Listener
{
    GameClient gameClient;

    public ClientListener(GameClient g)
    {
        super();

        gameClient = g;
    }

    public void connected(Connection connection)
    {
        Packets.RegisterName registerName = new Packets.RegisterName();
        registerName.name = gameClient.getPlayerName();
        connection.sendTCP(registerName);

        connection.sendTCP(new Packets.PlayerCountRequest());

        gameClient.setServerConnection(connection);
    }

    public void disconnected(Connection connection)
    {
        gameClient.updateCurrentScreen();
    }

    public void received(Connection connection, Object o)
    {
        if(gameClient.getCurrentScreen() instanceof MainMenuScreen)
        {
            if (o instanceof Packets.PlayerCount)
            {
                gameClient.setPlayerCountInfo((Packets.PlayerCount) o);
            } else if (o instanceof Packets.RegisterNameResponse)
            {
                //If the server didn't accept the name registration, then abort the connection
                Packets.RegisterNameResponse response = (Packets.RegisterNameResponse) o;

                switch (response.responseType)
                {
                    case ALREADY_HAS_NAME:
                        gameClient.abortConnection("You already have a name!");
                        break;
                    case NAME_EXISTS:
                        gameClient.abortConnection("Name taken by another player!");
                        break;
                }
            } else if (o instanceof Packets.GameSetup)
            {
                gameClient.startGame((Packets.GameSetup) o);
            }
        }
        else if(gameClient.getCurrentScreen() instanceof GameScreen)
        {
            if(o instanceof Packets.RoundResult)
            {
                ((GameScreen)gameClient.getCurrentScreen()).startNextRound((Packets.RoundResult)o);
            }
            else if(o instanceof Packets.OpponentChosen)
            {
                ((GameScreen)gameClient.getCurrentScreen()).opponentChosen();
            }
        }
    }
}
