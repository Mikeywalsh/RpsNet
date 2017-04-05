package com.rpsnet.game;

import com.esotericsoftware.kryonet.Client;
import com.rpsnet.network.NetworkHandler;

import java.io.IOException;
import java.util.Scanner;

public class GameClient extends Thread
{
    Client client;
    String name;

    boolean connected;

    public void run()
    {
        //Prompt the user for their name before connecting them
        System.out.println("Please enter your name...");
        Scanner scanner = new Scanner(System.in);
        name = scanner.nextLine();
        System.out.println("Hello " + name + "! Connecting to server...\n");

        //Create the client and start running it in another thread
        client = new Client();
        new Thread(client).start();

        //Register classes with Kryonet
        NetworkHandler.register(client);

        //Add the event listener to the client thread
        client.addListener(new ClientListener(this));

        //Attempt to connect to the server
        try
        {
            client.connect(5000, "127.0.0.1", NetworkHandler.port);
            connected = true;
        }
        catch (IOException ex)
        {
            System.out.println("Could not find server...");
            //System.exit(1);
        }
    }
}
