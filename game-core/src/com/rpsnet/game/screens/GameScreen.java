package com.rpsnet.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.rpsnet.game.GameClient;
import com.rpsnet.game.RPSNet;
import com.rpsnet.game.actors.GameActors;
import com.rpsnet.network.GameChoice;
import com.rpsnet.network.Packets;

public class GameScreen implements NetScreen
{
    RPSNet game;
    GameClient gameClient;
    Stage stage;
    SpriteBatch batch;
    GameActors gameActors;

    private Texture backgroundImg;
    private Texture rockTex;
    private Texture paperTex;
    private Texture scissorsTex;

    private Sprite playerChoice;
    private Sprite opponentChoice;

    private int gameID;
    private boolean roundFinishing;
    private Packets.RoundResult tempResult;
    private float roundFinishingTimer;
    private final float ROUND_FINISH_TIME = 1f;

    public GameScreen(RPSNet g, GameClient client, Packets.GameSetup setupInfo)
    {
        //Setup the stage
        game = g;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        //Setup the actors
        gameActors = new GameActors(this, setupInfo);
        stage.addActor(gameActors.getPlayerInfoWidgets());
        stage.addActor(gameActors.getOpponentInfoWidgets());
        stage.addActor(gameActors.getChoiceWidgets());

        //Assign SpriteBatch, textures and sprites
        batch = new SpriteBatch();
        backgroundImg = new Texture("background.jpg");
        rockTex = new Texture("rock.png");
        paperTex = new Texture("paper.png");
        scissorsTex = new Texture("scissors.png");

        //Assign GameClient and gameID
        gameClient = client;
        gameClient.setCurrentScreen(this);
        gameID = setupInfo.gameID;

        Gdx.gl.glClearColor(1, 0, 1, 1);
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(backgroundImg,0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if(roundFinishing)
        {
            //Increment the round finishing timer
            roundFinishingTimer += delta;

            //Draw both players choices
            if(playerChoice != null)
                playerChoice.draw(batch);
            if(opponentChoice != null)
                opponentChoice.draw(batch);

            //Move the choices towards each other
            if(playerChoice != null && opponentChoice != null && playerChoice.getX() < (Gdx.graphics.getWidth() / 2) + 64 && opponentChoice.getX() > (Gdx.graphics.getWidth() / 2) - 64)
            {
                playerChoice.translate(5, 0);
                opponentChoice.translate(-5, 0);
            }
            else
            {
                //Destroy the losing choices sprite when both sprites overlap
                switch (tempResult.winner)
                {
                    case 1:
                        opponentChoice = null;
                        break;
                    case 2:
                        playerChoice = null;
                        opponentChoice = null;
                        break;
                    case 3:
                        playerChoice = null;
                        break;
                }
            }

            //If the animation is over, go to the next round
            if(roundFinishingTimer > ROUND_FINISH_TIME)
            {
                roundFinishing = false;
                nextRound();
            }
        }
        batch.end();

        stage.act();
        stage.draw();
    }

    /**
     * Called when the user has made a choice to send to the server
     * @param choice The choice that the palyer has made
     */
    public void makeChoice(GameChoice choice)
    {
        gameActors.hideChoiceWidgets();
        gameClient.makeChoice(choice);
    }

    /**
     * Called when both players have made their choice and the server has sent a responce
     * @param result The result of the current round
     */
    public void startNextRound(Packets.RoundResult result)
    {
        //Draw the players choice
        switch (result.playerChoice)
        {
            case ROCK:
                playerChoice = new Sprite(rockTex);
                break;
            case PAPER:
                playerChoice = new Sprite(paperTex);
                break;
            case SCISSORS:
                playerChoice = new Sprite(scissorsTex);
                break;
        }
        playerChoice.setPosition((Gdx.graphics.getWidth() / 4) - 64, (Gdx.graphics.getHeight() / 2) - 32);

        //Draw the opponents choice
        switch (result.opponentChoice)
        {
            case ROCK:
                opponentChoice = new Sprite(rockTex);
                break;
            case PAPER:
                opponentChoice = new Sprite(paperTex);
                break;
            case SCISSORS:
                opponentChoice = new Sprite(scissorsTex);
                break;
        }
        opponentChoice.setPosition(Gdx.graphics.getWidth() - (Gdx.graphics.getWidth() / 4) - 64, (Gdx.graphics.getHeight() / 2) - 32);

        //Start the round finish animation
        roundFinishing = true;
        roundFinishingTimer = 0;
        tempResult = result;
    }

    /**
     * Moves the game to the next round after the round finish animation has completed
     */
    public void nextRound()
    {
        gameActors.nextRound(tempResult.playerScore, tempResult.opponentScore);
        tempResult = null;
    }

    /**
     * Called when the opponent has chosen but the player hasn't
     */
    public void opponentChosen()
    {
        gameActors.setOpponentStatusText("Chosen", Color.GREEN);
    }

    public void updateConnectionInfo(boolean connected)
    {
        //TEMP
        System.out.println("Connected: " + connected);
    }

    public void displayErrorMessage(String message)
    {
        //TEMP
        System.out.println(message);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose () {
        batch.dispose();
        backgroundImg.dispose();
        rockTex.dispose();
        paperTex.dispose();
        scissorsTex.dispose();
    }

    public int getGameID()
    {
        return gameID;
    }

}
