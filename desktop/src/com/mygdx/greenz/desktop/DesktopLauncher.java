package com.mygdx.greenz.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.greenz.MainGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = MainGame.V_WIDTH;
		config.height = MainGame.V_HEIGHT;

		new LwjglApplication(new MainGame(), config);
	}
}
