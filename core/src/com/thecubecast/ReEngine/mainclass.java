package com.thecubecast.ReEngine;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;
import com.thecubecast.ReEngine.Data.Common;
import com.thecubecast.ReEngine.Data.GameStateManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static com.badlogic.gdx.graphics.GL20.GL_NEAREST;

public class mainclass extends ApplicationAdapter implements InputProcessor{
	
	//The Drawing Variable
	private int W;
	private int H;

	private int SCALE = 2;

	private SpriteBatch batch;
	
	//Mouse Position in the window
	private int MouseX;
	private int MouseY;
	private int[] MouseDrag = new int[] {0, 0, 0};
	private int[] MouseClick = new int[] {0, 0, 0};
	
	// game state manager
	private GameStateManager gsm;

	// A variable for tracking elapsed time for the animation
	private float stateTime;
	
	@Override
	public void create () { // INIT FUNCTION

		Cursor customCursor = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("cursor0.png")), 0, 0);
		Gdx.graphics.setCursor(customCursor);
		
		stateTime = 0f;
		
		Gdx.input.setInputProcessor(this);
		
		//Just setting up the variables
		W = Gdx.graphics.getWidth();
		H = Gdx.graphics.getHeight();

		//This is essentially the graphics object we draw too
		batch = new SpriteBatch();
		
		
		
		//Figure out how to do this before you start exporting things to external files
		gsm = new GameStateManager();
	}
	
	@Override
	public void render () { // UPDATE Runs every frame. 60FPS

		//Gdx.gl.glClearColor( 1, 1, 1, 1 );
		Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
		
		//Code goes here
		
		UpdateInput();
		Update(); //UPDATE
	
		
		stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time

		//batch.begin();
		/*
		fb.bind();
		fb.begin();
		Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
		Draw(batch); //DRAW
		fb.end();

		fb.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

		//batch.end();
		batch.begin();
		batch.draw(fb.getColorBufferTexture(),0, H, FBOW*SCALE*2, -FBOH*SCALE);
		batch.end();
		*/

		Draw(batch);

		if(MouseClick[0] == 1) {
			MouseClick[0] = 0;
		}
	}
	
	public void UpdateInput(){

	}
	
	public void Update() {
		
		//Figure out how to do this before you start exporting things to external files
		gsm.update(MouseX, MouseY, MouseDrag, MouseClick);
	}
	
	public void Draw(SpriteBatch bbg) {
		//Figure out how to do this before you start exporting things to external files
		//gsm.draw(bbg, FBOW, FBOH, stateTime);
		gsm.draw(bbg, W, H, stateTime);
	}
	
	@Override
	public void resize(int width, int height) {
		W = width;
		H = height;
		//callback.setHeight(height);
		//callback.setWidth(width);
		Common.print("Ran Resize!");
		Common.print("" + width + " and H: " + height);
		gsm.reSize(batch, H, W);
	}
	
	
	@Override
	public void dispose () { //SHUTDOWN FUNCTION
		batch.dispose();
		gsm.dispose();
		//Common.ProperShutdown();
		//Cleanup(); SaveAll();
	}

	public boolean keyDown(int keycode) {return false;}
	public boolean keyUp(int keycode) {return false;}
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {return false;}


	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		MouseDrag[0] = 0;
		//Common.print("Clicked!");
		int[] MouseClicked = new int[] {1, MouseX, MouseY};
		MouseClick = MouseClicked;
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		//Common.print("Dragging");
			int[] MouseDraged = new int[] {1, screenX, screenY};
			MouseDrag = MouseDraged;
			MouseX = screenX;
			MouseY = screenY;
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		MouseX = screenX;
		MouseY = screenY;
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}