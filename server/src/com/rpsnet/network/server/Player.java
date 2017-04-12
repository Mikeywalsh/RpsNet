package com.rpsnet.network.server;

import com.rpsnet.network.GameChoice;

public class Player
{
    private RemoteClient remoteClient;
    private int score = 0;
    private GameChoice choice;

    public Player(RemoteClient r)
    {
        remoteClient = r;
        choice = null;
    }

    public void incrementScore()
    {
        score++;
    }

    public void makeChoice(GameChoice c)
    {
        choice = c;
    }

    public RemoteClient getRemoteClient()
    {
        return remoteClient;
    }

    public int getScore()
    {
        return score;
    }

    public GameChoice getChoice()
    {
        return choice;
    }
}
