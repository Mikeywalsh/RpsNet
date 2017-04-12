package com.rpsnet.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.rpsnet.game.screens.GameScreen;
import com.rpsnet.network.GameChoice;
import com.rpsnet.network.Packets;

public class GameActors implements Disposable
{
    private final GameScreen gameScreen;

    private final Skin skin;
    private final TextureAtlas uiAtlas;
    private final BitmapFont smallFont;
    private final BitmapFont bigFont;
    private final BitmapFont inputFont;

    private final TextButton.TextButtonStyle buttonStyle;
    private final Label.LabelStyle smallLabelStyle;
    private final Label.LabelStyle bigLabelStyle;

    private final Label playerNameText;
    private final Label playerScoreText;
    private final Label playerTurnStatusText;
    private final Label opponentNameText;
    private final Label opponentScoreText;
    private final Label opponentTurnStatusText;

    private final TextButton rockButton;
    private final TextButton paperButton;
    private final TextButton scissorsButton;

    private final Table playerInfoWidgets;
    private final Table opponentInfoWidgets;
    private final Table choiceWidgets;

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

        //Create Rock button
        rockButton = new TextButton("Rock", buttonStyle);
        rockButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.makeChoice(GameChoice.ROCK);
            }
        });

        //Create Paper Button
        paperButton = new TextButton("Paper", buttonStyle);
        rockButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.makeChoice(GameChoice.PAPER);
            }
        });

        //Create Scissors Button
        scissorsButton = new TextButton("Scissors", buttonStyle);
        rockButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.makeChoice(GameChoice.SCISSORS);
            }
        });

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

        //Create choice widgets
        choiceWidgets = new Table();
        choiceWidgets.setFillParent(true);

        //Add actors to the choice widgets
        choiceWidgets.bottom();
        choiceWidgets.add(rockButton).width(240).height(100);
        choiceWidgets.add(paperButton).width(240).height(100);
        choiceWidgets.add(scissorsButton).width(240).height(100);
    }

    /**
     * Hides the choice widgets
     */
    public void hideChoiceWidgets()
    {
        choiceWidgets.setVisible(false);
    }

    /**
     * Shows the choice widgets
     */
    public void showChoiceWidgets()
    {
        choiceWidgets.setVisible(true);
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

    /**
     * Gets the choice widgets
     * @return The choice widgets
     */
    public Table getChoiceWidgets()
    {
        return choiceWidgets;
    }

    @Override
    public void dispose()
    {
        uiAtlas.dispose();
    }
}
