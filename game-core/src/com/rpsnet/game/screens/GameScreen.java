package com.rpsnet.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.rpsnet.game.GameClient;
import com.rpsnet.game.RPSNet;
import com.rpsnet.network.Packets;

public class GameScreen implements NetScreen
{
    RPSNet game;
    GameClient gameClient;
    Stage stage;

    SpriteBatch batch;

    public GameScreen(RPSNet g, GameClient client, Packets.GameSetup setupInfo)
    {
        //Setup the stage
        game = g;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        //Assign SpriteBatch and textures
        batch = new SpriteBatch();

        //Assign GameClient
        gameClient = client;
        gameClient.setCurrentScreen(this);

        Gdx.gl.glClearColor(1, 0, 1, 1);
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
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
    }
}
