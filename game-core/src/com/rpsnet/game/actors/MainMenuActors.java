package com.rpsnet.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.rpsnet.network.ClientState;
import com.rpsnet.network.Packets;

public class MainMenuActors extends Table
{
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

    private final TextButton playButton;
    private final TextButton quitButton;

    private final Table disconnectedWidgets;
    private final Table connectedWidgets;
    private final Table menuWidgets;

    public MainMenuActors()
    {
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

        //Create the menu widgets
        menuWidgets = new Table();
        menuWidgets.setFillParent(true);

        //Add the actors to the menu
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

    public Table getDisconnectedWidgets()
    {
        return disconnectedWidgets;
    }

    public Table getConnectedWidgets() { return connectedWidgets; }

    public Table getMenuWidgets()
    {
        return menuWidgets;
    }
}
