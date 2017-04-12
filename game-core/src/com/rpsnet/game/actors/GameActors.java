package com.rpsnet.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Disposable;
import com.rpsnet.game.screens.GameScreen;
import com.rpsnet.game.screens.MainMenuScreen;
import com.rpsnet.network.Packets;

public class GameActors implements Disposable
{
    private final GameScreen gameScreen;

    private final Skin skin;
    private final TextureAtlas uiAtlas;
    private final BitmapFont smallFont;
    private final BitmapFont bigFont;
    private final BitmapFont inputFont;

    public final TextButton.TextButtonStyle buttonStyle;
    private final Label.LabelStyle smallLabelStyle;
    private final Label.LabelStyle bigLabelStyle;

    private final Label playerNameText;
    private final Label playerScoreText;
    private final Label playerTurnStatusText;
    private final Label opponentNameText;
    private final Label opponentScoreText;
    private final Label opponentTurnStatusText;

    private final Table playerInfoWidgets;
    private final Table opponentInfoWidgets;

    public GameActors(GameScreen screen, Packets.GameSetup gameInfo)
    {
        //Assign the main menu screen to its variable
        gameScreen = screen;

        //Create a freetype generator and parameter for text generation
        FreeTypeFontGenerator mainFontGen = new FreeTypeFontGenerator(Gdx.files.internal("UI/gameFont.ttf"));
        FreeTypeFontGenerator inputFontGen = new FreeTypeFontGenerator(Gdx.files.internal("UI/inputFont.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        //Create bitmap fonts from the generators and parameter
        fontParameter.size = 24;
        bigFont = mainFontGen.generateFont(fontParameter);
        fontParameter.size = 16;
        smallFont = mainFontGen.generateFont(fontParameter);
        inputFont = inputFontGen.generateFont(fontParameter);
        mainFontGen.dispose();
        inputFontGen.dispose();

        //Create a skin from the UI Atlas file
        skin = new Skin();
        uiAtlas = new TextureAtlas(Gdx.files.internal("UI/uiElements.atlas"));
        skin.addRegions(uiAtlas);

        //Create small button style
        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = smallFont;
        buttonStyle.up = skin.getDrawable("greenButtonUp");
        buttonStyle.down = skin.getDrawable("greenButtonDown");
        buttonStyle.over = skin.getDrawable("greenButtonOver");
        buttonStyle.disabled = skin.getDrawable("greenButtonDisabled");

        //Create label styles
        smallLabelStyle = new Label.LabelStyle();
        smallLabelStyle.font = smallFont;
        bigLabelStyle = new Label.LabelStyle();
        bigLabelStyle.font = bigFont;

        //Create Labels
        playerNameText = new Label(gameInfo.playerName, smallLabelStyle);
        playerScoreText = new Label("Score: 0", smallLabelStyle);
        playerTurnStatusText = new Label("Waiting...", smallLabelStyle);
        playerTurnStatusText.setColor(Color.RED);
        opponentNameText = new Label(gameInfo.opponentName, smallLabelStyle);
        opponentScoreText = new Label("Score: 0", smallLabelStyle);
        opponentTurnStatusText = new Label("Waiting...", smallLabelStyle);
        opponentTurnStatusText.setColor(Color.RED);

        //Create player widgets
        playerInfoWidgets = new Table();
        playerInfoWidgets.setFillParent(true);

        //Add actors to the player widgets
        playerInfoWidgets.left().top();
        playerInfoWidgets.add(playerNameText).left();
        playerInfoWidgets.row();
        playerInfoWidgets.add(playerScoreText).left();
        playerInfoWidgets.row();
        playerInfoWidgets.add(playerTurnStatusText).left();

        //Create player widgets
        opponentInfoWidgets = new Table();
        opponentInfoWidgets.setFillParent(true);

        //Add actors to the player widgets
        opponentInfoWidgets.right().top();
        opponentInfoWidgets.add(opponentNameText).right();
        opponentInfoWidgets.row();
        opponentInfoWidgets.add(opponentScoreText).right();
        opponentInfoWidgets.row();
        opponentInfoWidgets.add(opponentTurnStatusText).right();
    }

    /**
     * Gets the player info widgets
     * @return The player info widgets
     */
    public Table getPlayerInfoWidgets()
    {
        return playerInfoWidgets;
    }

    /**
     * Gets the opponent info widgets
     * @return The opponent info widgets
     */
    public Table getOpponentInfoWidgets()
    {
        return opponentInfoWidgets;
    }

    @Override
    public void dispose()
    {
        uiAtlas.dispose();
    }
}
