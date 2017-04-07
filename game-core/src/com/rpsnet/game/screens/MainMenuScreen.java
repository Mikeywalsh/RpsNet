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

public class MainMenuScreen implements Screen
{
    GameClient gameClient;
    Stage stage;
    SpriteBatch batch;
    Texture backgroundImg;
    MainMenuActors mainMenuActors;

    private final float CONNECTION_CHECK = 5;
    float checkTimer = 0;

    public MainMenuScreen() {
        //Setup the stage
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        //Setup the actors
        mainMenuActors = new MainMenuActors();
        stage.addActor(mainMenuActors.getConnectionWidgets());
        stage.addActor(mainMenuActors.getMenuWidgets());

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

        stage.draw();
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
