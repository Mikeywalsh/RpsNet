package com.rpsnet.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class NetworkHandler
{
    static public final int port = 54555;

    static public void register(EndPoint endPoint)
    {
        Kryo kryo = endPoint.getKryo();
        kryo.register(Packets.GameMessage.class);
        kryo.register(Packets.RegisterName.class);
    }
}
