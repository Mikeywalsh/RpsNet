package com.rpsnet.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

import java.util.HashMap;

public class NetworkHandler
{
    static public final int port = 54555;

    static public void register(EndPoint endPoint)
    {
        Kryo kryo = endPoint.getKryo();
        kryo.register(HashMap.class);
        kryo.register(ClientState.class);
        kryo.register(Packets.RegisterName.class);
        kryo.register(Packets.RequestPlayerCount.class);
        kryo.register(Packets.PlayerCount.class);
    }
}
