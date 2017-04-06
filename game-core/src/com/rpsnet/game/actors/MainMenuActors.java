package com.rpsnet.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenuActors extends Table
{
    private final Skin skin;
    private final TextureAtlas uiAtlas;
    private final BitmapFont font;

    private final TextButton.TextButtonStyle textButtonStyle;
    private final Label.LabelStyle labelStyle;

    private final FreeTypeFontGenerator generator;
    private final FreeTypeFontGenerator.FreeTypeFontParameter parameter;

    private final Label mainText;
    private final TextButton playButton;
    private final TextButton quitButton;

    public MainMenuActors()
    {
        //Create a freetype generator and parameter for text generation
        generator = new FreeTypeFontGenerator(Gdx.files.internal("gameFont.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        //Create a bitmapfont from the freetype font generator and then dispose the generator
        parameter.size = 24;
        font = generator.generateFont(parameter);
        generator.dispose();

        //Create a skin from the UI Atlas file
        skin = new Skin();
        uiAtlas = new TextureAtlas(Gdx.files.internal("UI/buttons.atlas"));
        skin.addRegions(uiAtlas);

        //Create button style
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("greenButtonUp");
        textButtonStyle.down = skin.getDrawable("greenButtonDown");

        //Create Play button
        playButton = new TextButton("Play", textButtonStyle);
        playButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("PLAY BUTTON PRESSED " + x + " " + y);
            }
        });

        //Create Quit button
        quitButton = new TextButton("Quit", textButtonStyle);
        quitButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        //Create label style
        labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        //Create main Label
        mainText = new Label("Rock Paper Scissors!", labelStyle);

        //Add the actors to the main menu
        add(mainText).padBottom(25);
        row();
        add(playButton).padBottom(5);
        row();
        add(quitButton);
    }
}
