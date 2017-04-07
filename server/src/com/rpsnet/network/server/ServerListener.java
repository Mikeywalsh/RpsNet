package com.rpsnet.network.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import com.rpsnet.network.Packets;

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
        Log.info("Client connected with ID: " + connection.getID());
        gameServer.addClient(connection);
    }

    public void disconnected(Connection connection)
    {

    }

    public void received(Connection connection, Object o)
    {
        //ClientGameConnection c = (ClientGameConnection)connection;
        System.out.println("Recieved packet: " + o);
        if(o instanceof Packets.RegisterName)
        {
//            if(c.name == null)
//            {
//                c.name = ((Packets.RegisterName)o).name;
            System.out.println("Client " + connection.getID() + " has registered name: " + ((Packets.RegisterName)o).name);
//            }
//            else
//            {
//                System.out.println("Client " + c.getID() + " tried to set a name, but already had one");
//            }
        }
    }
}
