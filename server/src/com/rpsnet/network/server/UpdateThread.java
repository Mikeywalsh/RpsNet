package com.rpsnet.network.server;

/**
 * Created by micha on 09/04/2017.
 */
public class UpdateThread extends Thread
{
    @Override
    public void run()
    {
        while(true)
        {
            System.out.println("Update check...");

            try
            {
                sleep(1000);
            }
            catch (Exception e)
            {
                System.out.println(e.getStackTrace());
            }
        }
    }
}
