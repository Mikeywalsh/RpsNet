package com.rpsnet.network.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import com.rpsnet.network.*;

public class ServerListener extends Listener
{
    GameServer gameServer;

    public ServerListener(GameServer g)
    {
        super();
        gameServer = g;
    }

    public void connected(Connection connection)
    {
        gameServer.addClient(connection);
        Log.info("Client connected with ID: " + connection.getID());
    }

    public void disconnected(Connection connection)
    {
        gameServer.removeClient(connection);
        Log.info("Removed client with ID: " + connection.getID());
    }

    public void received(Connection connection, Object o)
    {
        //System.out.println("Got packet: " + o);

        if(o instanceof Packets.RegisterName)
        {
            gameServer.registerName(connection, ((Packets.RegisterName)o).name);
        }
        else if(o instanceof Packets.PlayerCountRequest)
        {
            Packets.PlayerCount playerCount = new Packets.PlayerCount();
            playerCount.playerCount = gameServer.getPlayerCount();
            connection.sendTCP(playerCount);
        }
        else if(o instanceof Packets.MatchmakeRequest)
        {
            gameServer.queueClientMatchmaking(connection);
        }
        else if(o instanceof Packets.ChoiceMade)
        {
            gameServer.makeChoiceInGame(connection, (Packets.ChoiceMade)o);
        }
    }
}
