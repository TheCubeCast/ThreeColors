// GameState that shows logo.

package com.thecubecast.ReEngine.GameStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.thecubecast.ReEngine.Data.Common;
import com.thecubecast.ReEngine.Data.GameStateManager;
import com.thecubecast.ReEngine.Data.controlerManager;

public class IntroState extends GameState {
	
	private int alpha;
	private int ticks = 0;
	
	static final int WORLD_WIDTH = 100;
	static final int WORLD_HEIGHT = 100;

	
	private final int FADE_IN = 20;
	private final int LENGTH = 40;
	private final int FADE_OUT = 20;
	
	public IntroState(GameStateManager gsm) {
		super(gsm);
	}
	
	public void init() {
		//JukeBox.load("/Music/bgmusic.wav", "LogoSound");
		//JukeBox.play("LogoSound");


	}

	public void update() {
		handleInput();
		ticks++;
		if(ticks < FADE_IN) {
			alpha = (int) (255 - 255 * (1.0 * ticks / FADE_IN));
			if(alpha < 0) alpha = 0;
		}
		if(ticks > FADE_IN + LENGTH) {
			alpha = (int) (255 * (1.0 * ticks - FADE_IN - LENGTH) / FADE_OUT);
			if(alpha > 255) alpha = 255;
		}
		if(ticks > FADE_IN + LENGTH + FADE_OUT) {
			//JukeBox.stop("LogoSound");
			if (Gdx.input.isKeyPressed(Keys.D))
				gsm.Debug = true;
			if (gsm.ctm.isButtonDown(0, controlerManager.buttons.BUTTON_START) && gsm.ctm.isButtonDown(0, controlerManager.buttons.BUTTON_BACK))
				gsm.Debug = true;
			gsm.setState(GameStateManager.State.PLAY);
		}
	}
	
	public void draw(SpriteBatch g, int width, int height, float Time) {
		g.begin();
		Gdx.gl.glClearColor(255f, 255f, 255f, 1);
		
		
		gsm.Render.DrawSplash(g, 00, width/2, height/2, 0.4f, 0.4f, true);
		g.end();
	}
	
	public void handleInput() {

		if (Gdx.input.isKeyJustPressed(Keys.ENTER)) { //KeyHit
			//JukeBox.stop("LogoSound");
			gsm.Render.Images[00] = null;
			gsm.setState(GameStateManager.State.PLAY);
		}

	}
	
}