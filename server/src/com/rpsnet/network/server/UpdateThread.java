package com.rpsnet.network.server;

import java.util.List;

import static java.lang.Thread.sleep;

public class UpdateThread implements Runnable
{
    public GameServer gameServer;

    @Override
    public void run()
    {
        while(true)
        {
            try
            {
                gameServer.refreshPlayerCount();
                gameServer.broadcastPlayerCount();
                gameServer.attemptMatchmake();
                sleep(3000);
            }
            catch (Exception e)
            {
                System.out.println(e.getStackTrace());
            }
        }
    }


}
