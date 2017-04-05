package com.rpsnet.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen
{
    GameClient gameClient;

    SpriteBatch batch;
    Texture img;

    FreeTypeFontGenerator generator;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter;

    BitmapFont font;

    public GameScreen() {

        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        //font = new BitmapFont();
        generator = new FreeTypeFontGenerator(Gdx.files.internal("gameFont.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        //Create a bitmapfont from the freetype font generator and then dispose the generator
        parameter.size = 24;
        font = generator.generateFont(parameter);
        generator.dispose();

        gameClient = new GameClient();
        gameClient.start();
    }

    @Override
    public void render (float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        if(gameClient.connected) {
            font.draw(batch, "Welcome, " + gameClient.name, 200, 200);
        } else {
            font.draw(batch, "Input your name...", 200, 200);
        }
        batch.end();
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
        img.dispose();
    }
}
