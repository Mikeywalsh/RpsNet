package com.rpsnet.network;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds classes that will be transferred over the network
 */
public class Packets
{
    public static class RegisterName {
        public String name;
    }

    public static class RegisterNameResponse {
        public String requestedName;
        public ResponseType responseType;

        public enum ResponseType {

            ALREADY_HAS_NAME,
            NAME_EXISTS,
            ACCEPTED
        }
    }

    public static class PlayerCountRequest { }

    public static class PlayerCount {
        public Map<ClientState, Integer> playerCount;

        public int totalPlayerCount()
        {
            int sum = 0;

            for(int playersInState : playerCount.values())
            {
                sum += playersInState;
            }

            return sum;
        }
    }

    public static class MatchmakeRequest { }

    public static class GameSetup
    {
        public int gameID;
        public String opponentName;
    }
}
