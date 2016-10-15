package com.anecoz.br.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.anecoz.br.BRGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.foregroundFPS = 160;
		config.height = 720;
		config.width = 1280;
		new LwjglApplication(new BRGame(), config);
	}
}
