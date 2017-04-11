package com.rpsnet.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.rpsnet.game.RPSNet;

public class DesktopLauncher {
	public static void main (String[] arg) {
		TexturePacker.process("UI-Raw", "UI", "uiElements");

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new RPSNet(), config);
	}
}
