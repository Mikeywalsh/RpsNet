package com.rpsnet.network.server;

import static java.lang.Thread.sleep;

public class UpdateThread implements Runnable
{
    public GameServer gameServer;

    @Override
    public void run()
    {
        while(true)
        {
            System.out.println("Update check...");

            try
            {
                gameServer.refreshPlayerCount();
                gameServer.broadcastPlayerCount();
                sleep(3000);
            }
            catch (Exception e)
            {
                System.out.println(e.getStackTrace());
            }
        }
    }
}
