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

    private final Label scoreLimitText;
    private final Label playerNameText;
    private final Label playerScoreText;
    private final Label playerTurnStatusText;
    private final Label opponentNameText;
    private final Label opponentScoreText;
    private final Label opponentTurnStatusText;
    private final Label gameEndReasonText;

    private final TextButton rockButton;
    private final TextButton paperButton;
    private final TextButton scissorsButton;
    private final TextButton exitToMenuButton;

    private final Table gameInfoWidgets;
    private final Table playerInfoWidgets;
    private final Table opponentInfoWidgets;
    private final Table choiceWidgets;
    private final Table gameEndWidgets;

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
        scoreLimitText = new Label("Score Limit: " + gameInfo.scoreLimit, smallLabelStyle);
        playerNameText = new Label(gameInfo.playerName, smallLabelStyle);
        playerScoreText = new Label("Score: 0", smallLabelStyle);
        playerTurnStatusText = new Label("Waiting...", smallLabelStyle);
        playerTurnStatusText.setColor(Color.RED);
        opponentNameText = new Label(gameInfo.opponentName, smallLabelStyle);
        opponentScoreText = new Label("Score: 0", smallLabelStyle);
        opponentTurnStatusText = new Label("Waiting...", smallLabelStyle);
        opponentTurnStatusText.setColor(Color.RED);
        gameEndReasonText = new Label("null", smallLabelStyle);
        gameEndReasonText.setColor(Color.BLACK);

        //Create Rock button
        rockButton = new TextButton("Rock", buttonStyle);
        rockButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPlayerStatusText("Chosen", Color.GREEN);
                gameScreen.makeChoice(GameChoice.ROCK);
            }
        });

        //Create Paper Button
        paperButton = new TextButton("Paper", buttonStyle);
        paperButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPlayerStatusText("Chosen", Color.GREEN);
                gameScreen.makeChoice(GameChoice.PAPER);
            }
        });

        //Create Scissors Button
        scissorsButton = new TextButton("Scissors", buttonStyle);
        scissorsButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPlayerStatusText("Chosen", Color.GREEN);
                gameScreen.makeChoice(GameChoice.SCISSORS);
            }
        });

        //Create Exit To Menu Button
        exitToMenuButton = new TextButton("Exit To Menu", buttonStyle);
        exitToMenuButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.getGameInstance().exitToMenu();
            }
        });

        //Create game info widgets
        gameInfoWidgets = new Table();
        gameInfoWidgets.setFillParent(true);

        //Add actors to the game info widgets
        gameInfoWidgets.top();
        gameInfoWidgets.add(scoreLimitText);

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

        //Create end of game widgets
        gameEndWidgets = new Table();
        gameEndWidgets.setVisible(false);

        //Add actors to the end of game widgets
        gameEndWidgets.center();
        gameEndWidgets.add(gameEndReasonText);
        gameEndWidgets.row();
        gameEndWidgets.add(exitToMenuButton).width(150).height(50);
        gameEndWidgets.background(skin.getDrawable("greyPanel"));
        gameEndWidgets.setPosition((Gdx.graphics.getWidth() / 2) - 100, (Gdx.graphics.getHeight() / 2) - 45);
        gameEndWidgets.setWidth(200);
        gameEndWidgets.setHeight(90);
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
     * Sets the opponent turn status text
     * @param text The text to show
     * @param color The color of the text
     */
    public void setPlayerStatusText(String text, Color color)
    {
        playerTurnStatusText.setText(text);
        playerTurnStatusText.setColor(color);
    }

    /**
     * Sets the opponent turn status text
     * @param text The text to show
     * @param color The color of the text
     */
    public void setOpponentStatusText(String text, Color color)
    {
        opponentTurnStatusText.setText(text);
        opponentTurnStatusText.setColor(color);
    }

    /**
     * Updates the actors for the next game round, or ends the game if a player has won
     * @param result Information about the current rounds results
     */
    public void endRound(Packets.RoundResult result)
    {
        playerScoreText.setText("Score: " + result.playerScore);
        opponentScoreText.setText("Score: " + result.opponentScore);

        //Refresh turn status text and show choice widgets again
        setPlayerStatusText("Waiting...", Color.RED);
        setOpponentStatusText("Waiting...", Color.RED);
        showChoiceWidgets();

        //Show the end of game panel if the game is over
        if(result.gameOver)
        {
            if(result.winner == 1)
                endGame("You have won!");
            else
                endGame("You have lost!");
        }
    }

    /**
     * Called when the game is ending
     * Shows the gameEnd widgets and explains why the game is ending
     * @param endGameMessage
     */
    public void endGame(String endGameMessage)
    {
        hideChoiceWidgets();
        playerTurnStatusText.setVisible(false);
        opponentTurnStatusText.setVisible(false);
        gameEndReasonText.setText(endGameMessage);
        gameEndWidgets.setVisible(true);
    }

    /**
     * Gets the game info widgets
     * @return The game info widgets
     */
    public Table getGameInfoWidgets() { return gameInfoWidgets; }

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

    /**
     * Gets the game end widgets
     * @return The game end widgets
     */
    public Table getGameEndWidgets() { return gameEndWidgets; }

    @Override
    public void dispose()
    {
        uiAtlas.dispose();
    }
}
