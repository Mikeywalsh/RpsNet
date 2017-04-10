package com.rpsnet.network.server;

public class UpdateThread extends Thread
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
                sleep(3000);
            }
            catch (Exception e)
            {
                System.out.println(e.getStackTrace());
            }
        }
    }
}
