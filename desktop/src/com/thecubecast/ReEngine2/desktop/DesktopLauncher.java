package com.thecubecast.ReEngine2.desktop;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowListener;
import com.thecubecast.ReEngine.Data.Common;
import com.thecubecast.ReEngine.mainclass;

public class DesktopLauncher {
	public static void main (String[] args) {

		final Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration ();

		config.setWindowListener(new Lwjgl3WindowListener() {

			@Override
			public void created(Lwjgl3Window window) {

			}

			@Override
			public void iconified(boolean isIconified) {
				//Minimized
				Common.print("Minimized Window");
			}

			@Override
			public void maximized(boolean isMaximized) {

			}

			@Override
			public void focusLost() {

			}

			@Override
			public void focusGained() {

			}

			@Override
			public boolean closeRequested() {
				Common.print("clicked X");
				Common.ProperShutdown();
				return true;
			}

			@Override
			public void filesDropped(String[] files) {

			}

			@Override
			public void refreshRequested() {

			}
		});

		config.setWindowIcon("fish.png");
		config.setTitle("Jam 1");
		config.setResizable(false);
		config.setWindowedMode(896, 768);
		new Lwjgl3Application(new mainclass(), config);
	}
	
	public static int GetMonitorSizeW() {
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		return gd.getDisplayMode().getWidth();
	}
	public static int GetMonitorSizeH() {
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		return gd.getDisplayMode().getHeight();
	}
}
