package com.rpsnet.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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

import javax.xml.soap.Text;

public class MainMenuActors extends Table implements Disposable
{
    private final MainMenuScreen mainMenuScreen;

    private final Skin skin;
    private final TextureAtlas uiAtlas;
    private final BitmapFont smallFont;
    private final BitmapFont bigFont;
    private final BitmapFont inputFont;

    private final TextButton.TextButtonStyle largeButtonStyle;
    public final TextButton.TextButtonStyle smallButtonStyle;
    private final Label.LabelStyle smallLabelStyle;
    private final Label.LabelStyle bigLabelStyle;
    private final TextField.TextFieldStyle textFieldStyle;

    private final Label logoText;
    private final Label disconnectedText;
    private final Label connectionErrorText;

    private final Label welcomeText;
    private final Label totalPlayersText;
    private final Label ingamePlayersText;
    private final Label waitingPlayersText;
    private final Label matchmakingText;
    private final Label nameRequestText;

    private final TextButton connectButton;
    private final TextButton playButton;
    private final TextButton quitButton;

    private final AnimatedTexture loadingTexture;
    private final AnimatedActor loadingAnimation;

    private final Table disconnectedWidgets;
    private final Table connectedWidgets;
    private final Table menuWidgets;
    private final Table matchmakingWidgets;

    private final TextField nameInput;

    public MainMenuActors(MainMenuScreen menuScreen)
    {
        //Assign the main menu screen to its variable
        mainMenuScreen = menuScreen;

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

        //Create sarge button style
        largeButtonStyle = new TextButton.TextButtonStyle();
        largeButtonStyle.font = bigFont;
        largeButtonStyle.up = skin.getDrawable("greenButtonUp");
        largeButtonStyle.down = skin.getDrawable("greenButtonDown");
        largeButtonStyle.over = skin.getDrawable("greenButtonOver");
        largeButtonStyle.disabled = skin.getDrawable("greenButtonDisabled");

        //Create small button style
        smallButtonStyle = new TextButton.TextButtonStyle();
        smallButtonStyle.font = smallFont;
        smallButtonStyle.up = skin.getDrawable("greenButtonUp");
        smallButtonStyle.down = skin.getDrawable("greenButtonDown");
        smallButtonStyle.over = skin.getDrawable("greenButtonOver");
        smallButtonStyle.disabled = skin.getDrawable("greenButtonDisabled");

        //Create Connect button
        connectButton = new TextButton("Connect", smallButtonStyle);
        connectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                disableButton(connectButton);
                mainMenuScreen.connectButtonPressed(nameInput.getText());
            }
        });

        //Create Play button
        playButton = new TextButton("Play", largeButtonStyle);
        playButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menuWidgets.setVisible(false);
                matchmakingWidgets.setVisible(true);
                mainMenuScreen.playButtonPressed();
            }
        });
        disableButton(playButton);

        //Create Quit button
        quitButton = new TextButton("Quit", largeButtonStyle);
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
        nameRequestText = new Label("Enter Name: ", smallLabelStyle);
        welcomeText = new Label("Welcome, ", smallLabelStyle);
        totalPlayersText = new Label("Players online: -", smallLabelStyle);
        ingamePlayersText = new Label("Players ingame: -", smallLabelStyle);
        waitingPlayersText = new Label("Players queued: -", smallLabelStyle);
        matchmakingText = new Label("Finding game ", bigLabelStyle);
        connectionErrorText = new Label("", smallLabelStyle);

        //Create input field styles
        textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = inputFont;
        textFieldStyle.cursor = skin.getDrawable("inputCursor");
        textFieldStyle.background = skin.getDrawable("textFieldBackground");
        textFieldStyle.background.setLeftWidth(7);
        textFieldStyle.background.setRightWidth(7);
        textFieldStyle.fontColor = Color.WHITE;
        textFieldStyle.selection = skin.getDrawable("inputSelection");

        //Create input fields
        nameInput = new TextField("Player", textFieldStyle);
        nameInput.setMaxLength(12);

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
        disconnectedWidgets.add(disconnectedText).left().colspan(2);
        disconnectedWidgets.row();
        disconnectedWidgets.add(nameRequestText).left();
        disconnectedWidgets.add(nameInput).padRight(10);
        disconnectedWidgets.row();
        disconnectedWidgets.add(connectButton).colspan(2).width(100).height(35).left();
        disconnectedWidgets.row();
        disconnectedWidgets.add(connectionErrorText).left().colspan(2);

        //Create the connected widgets
        connectedWidgets = new Table();
        connectedWidgets.setFillParent(true);
        connectedWidgets.setVisible(false);

        //Add the actors to the connected widget
        connectedWidgets.left();
        connectedWidgets.top();
        connectedWidgets.add(welcomeText).left();
        connectedWidgets.row();
        connectedWidgets.add(totalPlayersText).left();
        connectedWidgets.row();
        connectedWidgets.add(ingamePlayersText).left();
        connectedWidgets.row();
        connectedWidgets.add(waitingPlayersText).left();

        //Create the matchmaking widgets
        matchmakingWidgets = new Table();
        matchmakingWidgets.setFillParent(true);
        matchmakingWidgets.setVisible(false);

        //Add the actors to the connected widget
        matchmakingWidgets.add(matchmakingText);
        matchmakingWidgets.add(loadingAnimation).padRight(100);
    }

    private void updateWelcomeText(String val)
    {
        welcomeText.setText(val);
    }

    private void updatePlayerCounts(Packets.PlayerCount info)
    {
        if(info == null)
            return;

        totalPlayersText.setText("Players online: " + info.totalPlayerCount());
        ingamePlayersText.setText("Players ingame: " + info.playerCount.get(ClientState.INGAME));
        waitingPlayersText.setText("Players queued: " + info.playerCount.get(ClientState.QUEUED));
    }

    public void enableButton(TextButton button)
    {
        button.setTouchable(Touchable.enabled);
        button.setDisabled(false);
    }

    public void disableButton(TextButton button)
    {
        button.setTouchable(Touchable.disabled);
        button.setDisabled(true);
    }

    public void updateConnectionInfo(boolean isConnected, String playerName, Packets.PlayerCount playerCount)
    {
        if(isConnected)
        {
            enableButton(playButton);
            updateWelcomeText("Welcome " + playerName + "!");
            updatePlayerCounts(playerCount);
            disconnectedWidgets.setVisible(false);
            connectedWidgets.setVisible(true);
        }
        else
        {
            disableButton(playButton);
            enableButton(connectButton);
            disconnectedWidgets.setVisible(true);
            connectedWidgets.setVisible(false);
        }
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
