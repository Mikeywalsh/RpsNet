package com.rpsnet.network.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.minlog.Log;
import com.rpsnet.network.*;

public class RemoteClient
{
    private Connection connection;
    private String name;
    private ClientState clientState;

    public RemoteClient(Connection c)
    {
        connection = c;
        clientState = ClientState.NAMELESS;
    }

    public Connection getConnection() { return connection; }

    public String getName() { return name; }

    public ClientState getClientState() { return clientState; }

    public void setName(String val)
    {
        if(clientState == ClientState.NAMELESS)
        {
            name = val;
            clientState = ClientState.READY;
        }
        else
        {
            Log.error("Tried to set a name for a nameless client!");
        }
    }
}
