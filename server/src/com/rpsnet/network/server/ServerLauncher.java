package com.rpsnet.network.server;

import java.io.IOException;

public class ServerLauncher
{
	public static void main(String[] args){
		try
		{
			new GameServer();
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
	}
}
