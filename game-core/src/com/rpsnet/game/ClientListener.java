package com.rpsnet.game;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
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

    }

    public void received(Connection connection, Object o)
    {
        if(o instanceof Packets.PlayerCount)
        {
            gameClient.setPlayerCountInfo((Packets.PlayerCount)o);
        }
    }
}
