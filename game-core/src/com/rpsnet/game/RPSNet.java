package com.rpsnet.game;

import com.badlogic.gdx.Game;
import com.rpsnet.game.screens.MainMenuScreen;

public class RPSNet extends Game
{
	@Override
	public void create () {
		setScreen(new MainMenuScreen());
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}
}
