package com.rpsnet.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.rpsnet.game.AnimatedTexture;
import com.rpsnet.game.screens.MainMenuScreen;
import com.rpsnet.network.ClientState;
import com.rpsnet.network.Packets;

public class MainMenuActors extends Table implements Disposable
{
    private final MainMenuScreen mainMenuScreen;

    private final Skin skin;
    private final TextureAtlas uiAtlas;
    private final BitmapFont smallFont;
    private final BitmapFont bigFont;

    private final TextButton.TextButtonStyle textButtonStyle;
    private final Label.LabelStyle smallLabelStyle;
    private final Label.LabelStyle bigLabelStyle;

    private final FreeTypeFontGenerator generator;
    private final FreeTypeFontGenerator.FreeTypeFontParameter parameter;

    private final Label logoText;
    private final Label disconnectedText;

    private final Label welcomeText;
    private final Label totalPlayersText;
    private final Label ingamePlayersText;
    private final Label waitingPlayersText;
    private final Label matchmakingText;

    private final TextButton playButton;
    private final TextButton quitButton;

    private final AnimatedTexture loadingTexture;
    private final AnimatedActor loadingAnimation;

    private final Table disconnectedWidgets;
    private final Table connectedWidgets;
    private final Table menuWidgets;
    private final Table matchmakingWidgets;

    public MainMenuActors(MainMenuScreen menuScreen)
    {
        //Assign the main menu screen to its variable
        mainMenuScreen = menuScreen;

        //Create a freetype generator and parameter for text generation
        generator = new FreeTypeFontGenerator(Gdx.files.internal("gameFont.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        //Create a bitmapfont from the freetype font generator and then dispose the generator
        parameter.size = 24;
        bigFont = generator.generateFont(parameter);
        parameter.size = 16;
        smallFont = generator.generateFont(parameter);
        generator.dispose();

        //Create a skin from the UI Atlas file
        skin = new Skin();
        uiAtlas = new TextureAtlas(Gdx.files.internal("UI/buttons.atlas"));
        skin.addRegions(uiAtlas);

        //Create button style
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = bigFont;
        textButtonStyle.up = skin.getDrawable("greenButtonUp");
        textButtonStyle.down = skin.getDrawable("greenButtonDown");
        textButtonStyle.over = skin.getDrawable("greenButtonOver");
        textButtonStyle.disabled = skin.getDrawable("greenButtonDisabled");

        //Create Play button
        playButton = new TextButton("Play", textButtonStyle);
        playButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menuWidgets.setVisible(false);
                matchmakingWidgets.setVisible(true);
                mainMenuScreen.playButtonPressed();
            }
        });
        disablePlayButton();

        //Create Quit button
        quitButton = new TextButton("Quit", textButtonStyle);
        quitButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        //Create label styles
        smallLabelStyle = new Label.LabelStyle();
        smallLabelStyle.font = smallFont;
        bigLabelStyle = new Label.LabelStyle();
        bigLabelStyle.font = bigFont;

        //Create Labels
        logoText = new Label("Rock Paper Scissors!", bigLabelStyle);
        disconnectedText = new Label("Not connected", smallLabelStyle);
        welcomeText = new Label("Welcome, ", smallLabelStyle);
        totalPlayersText = new Label("Players online: -", smallLabelStyle);
        ingamePlayersText = new Label("Players ingame: -", smallLabelStyle);
        waitingPlayersText = new Label("Players queued: -", smallLabelStyle);
        matchmakingText = new Label("Finding game ", bigLabelStyle);

        //Create animated textures
        loadingTexture = new AnimatedTexture("UI/loadingSheet.png", 4, 2, 0.03f);
        loadingAnimation = new AnimatedActor(loadingTexture);

        //Create the menu widgets
        menuWidgets = new Table();
        menuWidgets.setFillParent(true);

        //Add the actors to the menu widgets
        menuWidgets.add(logoText).padBottom(25);
        menuWidgets.row();
        menuWidgets.add(playButton).padBottom(5);
        menuWidgets.row();
        menuWidgets.add(quitButton);

        //Create the disconnected widgets
        disconnectedWidgets = new Table();
        disconnectedWidgets.setFillParent(true);

        //Add the actors to the disconnected widget
        disconnectedWidgets.left();
        disconnectedWidgets.top();
        disconnectedWidgets.add(disconnectedText);

        //Create the connected widgets
        connectedWidgets = new Table();
        connectedWidgets.setFillParent(true);
        connectedWidgets.setVisible(false);

        //Add the actors to the connected widget
        connectedWidgets.left();
        connectedWidgets.top();
        connectedWidgets.add(welcomeText);
        connectedWidgets.row();
        connectedWidgets.add(totalPlayersText);
        connectedWidgets.row();
        connectedWidgets.add(ingamePlayersText);
        connectedWidgets.row();
        connectedWidgets.add(waitingPlayersText);

        //Create the matchmaking widgets
        matchmakingWidgets = new Table();
        matchmakingWidgets.setFillParent(true);
        matchmakingWidgets.setVisible(false);

        //Add the actors to the connected widget
        matchmakingWidgets.add(matchmakingText);
        matchmakingWidgets.add(loadingAnimation).padRight(100);
    }

    public void updateWelcomeText(String val)
    {
        welcomeText.setText(val);
    }

    public void updatePlayerCounts(Packets.PlayerCount info)
    {
        if(info == null)
            return;

        totalPlayersText.setText("Players online: " + info.totalPlayerCount());
        ingamePlayersText.setText("Players ingame: " + info.playerCount.get(ClientState.INGAME));
        waitingPlayersText.setText("Players queued: " + info.playerCount.get(ClientState.WAITING));
    }

    public void enablePlayButton()
    {
        playButton.setTouchable(Touchable.enabled);
        playButton.setDisabled(false);
    }

    public void disablePlayButton()
    {
        playButton.setTouchable(Touchable.disabled);
        playButton.setDisabled(true);
    }

    public Table getDisconnectedWidgets()
    {
        return disconnectedWidgets;
    }

    public Table getConnectedWidgets() { return connectedWidgets; }

    public Table getMenuWidgets()
    {
        return menuWidgets;
    }

    public Table getMatchmakingWidgets() { return matchmakingWidgets; }

    @Override
    public void dispose()
    {
        uiAtlas.dispose();
        loadingTexture.dispose();
    }
}
