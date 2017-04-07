package com.rpsnet.network.server;

import com.esotericsoftware.kryonet.Connection;

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
}
