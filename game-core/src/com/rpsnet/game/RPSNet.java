package com.rpsnet.game;

import com.badlogic.gdx.Game;

public class RPSNet extends Game
{
	@Override
	public void create () {
		setScreen(new GameScreen());
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
