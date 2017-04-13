package com.rpsnet.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.*;
import com.rpsnet.game.GameClient;
import com.rpsnet.game.RPSNet;
import com.rpsnet.game.actors.MainMenuActors;
import com.rpsnet.network.Packets;

public class MainMenuScreen implements Screen, NetScreen
{
    RPSNet game;
    GameClient gameClient;

    Stage stage;
    SpriteBatch batch;
    Texture backgroundImg;
    MainMenuActors mainMenuActors;

    public MainMenuScreen(RPSNet g, GameClient client) {

        //Assign SpriteBatch and textures
        batch = new SpriteBatch();
        backgroundImg = new Texture("Sprites/background.jpg");

        //Setup the stage
        game = g;
        stage = new Stage(new ScreenViewport(), batch);
        Gdx.input.setInputProcessor(stage);

        //Setup the actors
        mainMenuActors = new MainMenuActors(this);
        stage.addActor(mainMenuActors.getDisconnectedWidgets());
        stage.addActor(mainMenuActors.getConnectedWidgets());
        stage.addActor(mainMenuActors.getMenuWidgets());
        stage.addActor(mainMenuActors.getMatchmakingWidgets());

        //Assign GameClient
        gameClient = client;
        gameClient.setCurrentScreen(this);
        gameClient.updateCurrentScreen();

        Gdx.gl.glClearColor(1, 0, 1, 1);
    }

    @Override
    public void render (float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(backgroundImg,0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void updateConnectionInfo(boolean connected)
    {
        if (connected)
        {
            mainMenuActors.updateConnectionInfo(true, gameClient.getPlayerName(), gameClient.getPlayerCountInfo());
        } else
        {
            mainMenuActors.updateConnectionInfo(false, null, null);
        }
    }

    @Override
    public void displayErrorMessage(String message)
    {
        mainMenuActors.displayErrorMessage(message);
    }

    /**
     * Called when the connect button is pressed
     * Attempts to connect to the remote server through the gameClient with the input address and player name
     * @param name The input name of the player
     */
    public void connectButtonPressed(String ipAdress, String name)
    {
        //Reset the error message if there is one and attempt to connect
        displayErrorMessage("");
        gameClient.attemptConnection(ipAdress, name);
    }

    /**
     * Called when the play button is pressed
     * Tells the server the client wishes to queue the player for matchmaking
     */
    public void playButtonPressed()
    {
        gameClient.requestMatchmake();
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
    }

    public RPSNet getGame()
    {
        return game;
    }
}
