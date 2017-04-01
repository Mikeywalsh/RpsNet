import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

public class GameServer
{
    Server server;

    public GameServer() throws IOException
    {
        //Create the server object
        server = new Server(){};
        System.out.println("Server started...");

        //Register classes with Kryonet
        Network.register(server);

        //Add a listener to the server and bind the port
        server.addListener(new ServerListener());
        server.bind(Network.port);

        //Start running the server
        server.start();
    }

    public static void main(String[] args) throws IOException{
        new GameServer();
    }
}
