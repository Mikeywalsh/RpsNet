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
        System.out.println("Connected to server...");

        Packets.RegisterName registerName = new Packets.RegisterName();
        registerName.name = gameClient.name;
        connection.sendTCP(registerName);
    }

    public void disconnected(Connection connection)
    {

    }

    public void received(Connection connection, Object o)
    {

    }
}
