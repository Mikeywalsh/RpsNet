package com.rpsnet.game;

import com.badlogic.gdx.Game;
import com.rpsnet.game.screens.GameScreen;
import com.rpsnet.game.screens.MainMenuScreen;
import com.rpsnet.network.Packets;

/**
 * The entry point for the game
 * Initialises the game client and sets the screen to the menu screen
 */
public class RPSNet extends Game
{
	GameClient gameClient;

	/**
	 * Other threads can flag this to allow the start of games from outside the rendering thread
	 */
	private boolean startGameFlag;

	/**
	 * Other threads can set this to allow the start of games from outside the rendering thread
	 */
	private Packets.GameSetup startGameInfo;

	public void create () {
		gameClient = new GameClient();
		setMenuScreen();
	}

	@Override
	public void render () {
		super.render();

		//If another thread has flagged the game to begin, then swap to the game screen
		if(startGameFlag)
		{
			setGameScreen(startGameInfo);
			startGameFlag = false;
		}
	}

	private void setMenuScreen()
	{
		setScreen(new MainMenuScreen(this, gameClient));
	}

	private void setGameScreen(Packets.GameSetup setupInfo)
	{
		setScreen(new GameScreen(this, gameClient, setupInfo));
	}

	/**
	 * Other threads can call this to allow the start of games from outside the rendering thread
	 */
	public void setGameInfo(Packets.GameSetup setupInfo)
	{
		startGameFlag = true;
		startGameInfo = setupInfo;
	}

	@Override
	public void dispose () {}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}
}
