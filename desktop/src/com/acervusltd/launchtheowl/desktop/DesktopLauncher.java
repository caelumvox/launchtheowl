package com.acervusltd.launchtheowl.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.acervusltd.launchtheowl.LaunchTheOwlGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Launch the Owl";
		config.width = 800;
		config.height = 480;
		new LwjglApplication(new LaunchTheOwlGame(), config);
	}
}
