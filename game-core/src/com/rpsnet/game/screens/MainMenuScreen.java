package com.rpsnet.game.screens;

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
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.rpsnet.game.GameClient;
import com.rpsnet.game.actors.MainMenuActors;
import com.rpsnet.network.Packets;

public class MainMenuScreen implements Screen
{
    GameClient gameClient;
    Stage stage;
    SpriteBatch batch;
    Texture backgroundImg;
    MainMenuActors mainMenuActors;

    private final float CONNECTION_REFRESH_TIME = 5;
    float refreshTimer = 0;

    public MainMenuScreen() {
        //Setup the stage
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        //Setup the actors
        mainMenuActors = new MainMenuActors(this);
        stage.addActor(mainMenuActors.getDisconnectedWidgets());
        stage.addActor(mainMenuActors.getConnectedWidgets());
        stage.addActor(mainMenuActors.getMenuWidgets());
        stage.addActor(mainMenuActors.getMatchmakingWidgets());

        batch = new SpriteBatch();
        backgroundImg = new Texture("background.jpg");

        gameClient = new GameClient();
        gameClient.start();

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

        //Update information that depends on the server connection
        refreshTimer += delta;
        if(refreshTimer > CONNECTION_REFRESH_TIME)
        {
            refreshTimer -= CONNECTION_REFRESH_TIME;
            mainMenuActors.getConnectedWidgets().setVisible(gameClient.connected());
            mainMenuActors.getDisconnectedWidgets().setVisible(!gameClient.connected());

            if(gameClient.connected())
            {
                gameClient.requestPlayerCount();
                mainMenuActors.updateWelcomeText("Welcome, " + gameClient.getPlayerName());
                mainMenuActors.updatePlayerCounts(gameClient.getPlayerCountInfo());
                mainMenuActors.enablePlayButton();
            }
        }
    }

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
}
