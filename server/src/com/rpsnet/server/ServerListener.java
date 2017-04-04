package com.rpsnet.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.rpsnet.Packets;

public class ServerListener extends Listener
{
    public void connected(Connection connection)
    {
        System.out.println("Client connected with ID: " + connection.getID());
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
