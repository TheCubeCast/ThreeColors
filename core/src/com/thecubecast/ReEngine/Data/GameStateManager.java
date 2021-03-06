// The GameStateManager does exactly what its
// name says. It contains a list of GameStates.
// It decides which GameState to update() and
// draw() and handles switching between different
// GameStates.

package com.thecubecast.ReEngine.Data;

import com.thecubecast.ReEngine.GameStates.*;
import com.thecubecast.ReEngine.Graphics.Draw;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

import java.awt.*;

public class GameStateManager {

    public controlerManager ctm;

	public boolean Debug = false;

    public enum State {
        INTRO, PLAY, LOADING
    }

    public State newcurrentState;
    private State newpreviousState;


    public GameState gameState;
	
	public String Username;
	public String IpAdress = "localhost";

	public float CurrentTime;
	public float DeltaTime;
	
	//Public render function object
	public Draw Render;
	public int ticks = 0;

	public Discord DiscordManager;

	//MousePos
	public int MouseX;
	public int MouseY;
	public int[] MouseDrag;
	public int[] MouseClick;
	public int OldCursor = 0;
	public int Cursor = 0;
	
	//screen
	public int Width;
	public int Height;
	
	public GameStateManager() {

        ctm = new controlerManager();

		DiscordManager = new Discord("417486353006067743");

		Render = new Draw();

		Render.Init();

		//setState(Blank);
		LoadState("STARTUP"); //THIS IS THE STATE WERE WE START WHEN THE GAME IS RUN
		
	}

	public void LoadState(String LoadIt) {
		newpreviousState = newcurrentState;
		unloadState();
		newcurrentState = State.LOADING;
		//Set up the loading state
		gameState = new LoadingState(this);
		((LoadingState) gameState).setLoad("STARTUP");
		gameState.init();
	}
	
	public void setState(State i) {
		newpreviousState = newcurrentState;
		unloadState();
        newcurrentState = i;
        switch (newcurrentState) {
            case INTRO:
                Common.print("Loaded state Intro");
                gameState = new IntroState(this);
                gameState.init();
                break;
            case PLAY:
                Common.print("Loaded state PLAY");
                gameState = new blankTestState(this);
                gameState.init();
                break;
			case LOADING:
				break;
		}
		
	}
	
	public void unloadState() {
		//Common.print("Unloaded state " + i);
		gameState = null;
	}
	
	public void update(int MousX, int MousY, int[] Draging, int[] MousCl) {
		ticks++;
		if (Cursor != OldCursor) {
			OldCursor = Cursor;
			com.badlogic.gdx.graphics.Cursor customCursor = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("cursor" + Cursor + ".png")), 0, 0);
    		Gdx.graphics.setCursor(customCursor);
		}
		MouseX = MousX;
		MouseY = MousY;
		MouseDrag = Draging;
		MouseClick = MousCl;
		if(gameState != null) {
			
			gameState.update();
		}
		//MouseClick[0] = 0;

		DiscordManager.UpdatePresence();
		ctm.update();
	}
	
	public void draw(SpriteBatch bbg, int W, int H, float Time) {
		Width = W;
		Height = H;
		CurrentTime = Time;
		DeltaTime = Math.min(Gdx.graphics.getDeltaTime(), 1f / 60f);
		if(gameState != null) {
			gameState.draw(bbg, H, W, Time);
		}
	}
	
	public void reSize(SpriteBatch bbg, int H, int W) {
		if(gameState != null) {
			gameState.reSize(bbg, H, W);
		}
		Matrix4 matrix = new Matrix4();
		matrix.setToOrtho2D(0, 0, W, H);
		bbg.setProjectionMatrix(matrix);
	}

	public void dispose() {
		DiscordManager.dispose();
	}
}
