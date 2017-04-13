package com.rpsnet.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.rpsnet.game.RPSNet;

public class DesktopLauncher {
	public static void main (String[] arg) {
		//TexturePacker.process("UI-Raw", "UI", "uiElements");

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = 720;
		config.height = 480;
		config.resizable = false;
		config.addIcon("Icons/iconSmall.png", Files.FileType.Internal);
		config.addIcon("Icons/iconMedium.png", Files.FileType.Internal);
		config.addIcon("Icons/iconLarge.png", Files.FileType.Internal);
		new LwjglApplication(new RPSNet(), config);
	}
}
