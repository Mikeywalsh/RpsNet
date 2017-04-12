package com.rpsnet.network.server;

import com.esotericsoftware.minlog.Log;
import com.rpsnet.network.GameChoice;
import com.rpsnet.network.Packets;

/**
 * An instance of a Rock-Paper-Scissors game
 * Used for the server to keep track of individual games
 */
public class GameInstance
{
    private GameServer gameServer;
    private int gameID;
    private Player player1;
    private Player player2;

    private final int SCORE_LIMIT = 3;

    public GameInstance(GameServer server, int id, RemoteClient client1, RemoteClient client2)
    {
        gameServer = server;
        gameID = id;
        player1 = new Player(client1);
        player2 = new Player(client2);
    }

    /**
     * Makes a choice for a player in this game instance
     * Also checks to see if the round/game has ended as a result
     * @param player The player making the choice
     * @param choice The choice being made
     */
    public void makeChoice(RemoteClient player, GameChoice choice)
    {
        if(player1.getRemoteClient() != player && player2.getRemoteClient() != player)
            Log.error("Client with name: " + player.getPlayerName() + " tried to make a choice in a game they are not in!");

        //Make the choice and alert the opposing player
        if(player1.getRemoteClient() == player)
        {
            player1.makeChoice(choice);
            player2.getRemoteClient().getConnection().sendTCP(new Packets.OpponentChosen());
        }
        else if(player2.getRemoteClient() == player)
        {
            player2.makeChoice(choice);
            player1.getRemoteClient().getConnection().sendTCP(new Packets.OpponentChosen());
        }

        if(player1.getChoice() != null && player2.getChoice() != null)
        {
            Player winner = determineWinner();
            Boolean gameOver = false;

            if(winner != null)
            {
                winner.incrementScore();
                if(winner.getScore() == SCORE_LIMIT)
                    gameOver = true;
            }

            Packets.RoundResult roundResult = new Packets.RoundResult();
            roundResult.gameOver = gameOver;

            //Create a response for player 1
            roundResult.playerScore = player1.getScore();
            roundResult.opponentScore = player2.getScore();
            roundResult.playerChoice = player1.getChoice();
            roundResult.opponentChoice = player2.getChoice();
            if(winner == player1)
                roundResult.winner = 1;
            else if(winner == null)
                roundResult.winner = 2;
            else
                roundResult.winner = 3;

            //Send the response to player 1
            player1.getRemoteClient().getConnection().sendTCP(roundResult);

            //Create a response for player 2
            roundResult.playerScore = player2.getScore();
            roundResult.opponentScore = player1.getScore();
            roundResult.playerChoice = player2.getChoice();
            roundResult.opponentChoice = player1.getChoice();
            if(winner == player2)
                roundResult.winner = 1;
            else if(winner == null)
                roundResult.winner = 2;
            else
                roundResult.winner = 3;

            //Send the response to player 2
            player2.getRemoteClient().getConnection().sendTCP(roundResult);

            //Refresh both players choices
            player1.refreshChoice();
            player2.refreshChoice();

            //Remove this game from the servers list of active games if a player has won
            if(gameOver)
                gameServer.gameFinished(gameID);
        }
    }

    /**
     * Determines the winner of the round when both players have made their turn
     * @return The player instance who won, null if there was a draw
     */
    private Player determineWinner()
    {
        if(player1.getChoice() == null || player2.getChoice() == null)
            Log.error("Still waiting for both players to make a choice!");

        if(player1.getChoice() == player2.getChoice())
            return null;

        if(player1.getChoice() == GameChoice.ROCK && player2.getChoice() == GameChoice.SCISSORS)
            return player1;

        if(player1.getChoice() == GameChoice.PAPER && player2.getChoice() == GameChoice.ROCK)
            return player1;

        if(player1.getChoice() == GameChoice.SCISSORS && player2.getChoice() == GameChoice.PAPER)
            return player1;

        return player2;
    }
}
