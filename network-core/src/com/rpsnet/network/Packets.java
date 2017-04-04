package com.rpsnet.network;

/**
 * Holds classes that will be transferred over the network
 */
public class Packets
{
    public static class GameMessage {
        public String message;
    }

    public static class RegisterName {
        public String name;
    }
}
