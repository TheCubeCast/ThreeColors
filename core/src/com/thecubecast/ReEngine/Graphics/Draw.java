package com.thecubecast.ReEngine.Graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.thecubecast.ReEngine.Data.Common;
import com.thecubecast.ReEngine.Data.GameStateManager;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Draw {

	public AssetManager manager = new AssetManager();

	//Animation Variables
	public Animation<TextureRegion> LoadingAnimation; // Must declare frame type (TextureRegion)
	Texture LoadingSheet;

	
	//Always set to 1 above the number of spites in file
	public Texture[] Tiles;
	public Texture[] Images;
	
	BitmapFont font = new BitmapFont();
	
	public void Init() {
		
		// Initialize the Animation with the frame interval and array of frames
		LoadingAnimation = new Animation<TextureRegion>(0.1f, loadAnim(LoadingSheet, "cube_loading_sprite.png", 4, 1));
		
	}
	
	public void Load() {
		//The loops bellow grab the tiles and add them to the variable

		Path ImagePath = Paths.get("Images");

		try {
			Images = new Texture[(int) Files.list(ImagePath).count()];
		} catch (IOException e) {
			e.printStackTrace();
		}

		for(int i=0; i < Images.length; ++i){
			if (i >= 10) {
				try {
					manager.load(ImagePath + "/image_"+ Integer.toString(i) +".png", Texture.class);
					Images[i] = manager.get(ImagePath + "/image_"+ Integer.toString(i) +".png", Texture.class);
				}
				catch(Exception e) {
					//e.printStackTrace();
				}
			} else {
				try {
					manager.load(ImagePath + "/image_0"+ Integer.toString(i) +".png", Texture.class);
					Images[i] = manager.get(ImagePath + "/image_0"+ Integer.toString(i) +".png", Texture.class);
				}
				catch(Exception e) {
					//e.printStackTrace();
				}
			}
        }

		try {
			GraphicsEnvironment ge = 
			         GraphicsEnvironment.getLocalGraphicsEnvironment();
			     ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Fonts/Munro.ttf")));
		}
		catch(Exception e) {
			//e.printStackTrace();
		}
	}

	public void LoadVariables() {
		//The loops bellow grab the tiles and add them to the variable
		for(int i=0; i < Images.length; ++i){
			if (i >= 10) {
				try {
					Common.print("Loaded images /Images/"+ Integer.toString(i) +".png");
					Images[i] = manager.get("Images/image_"+ Integer.toString(i) +".png", Texture.class);
				}
				catch(Exception e) {
					//e.printStackTrace();
				}
			} else {
				try {
					Common.print("Loaded images /Images/0"+ Integer.toString(i) +".png");
					Images[i] = manager.get("Images/image_0"+ Integer.toString(i) +".png", Texture.class);
				}
				catch(Exception e) {
					//e.printStackTrace();
				}
			}
		}

		try {
			GraphicsEnvironment ge =
					GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Fonts/Munro.ttf")));
		}
		catch(Exception e) {
			//e.printStackTrace();
		}
	}

	public TextureRegion[] loadAnim(Texture TexSheet, String FileLocation, int Cols, int Rows) {
		// Load the sprite sheet as a Texture
		TexSheet = new Texture(Gdx.files.internal(FileLocation));

		// Use the split utility method to create a 2D array of TextureRegions. This is 
		// possible because this sprite sheet contains frames of equal size and they are 
		// all aligned.
		TextureRegion[][] tmp = TextureRegion.split(TexSheet, 
				TexSheet.getWidth() / Cols,
				TexSheet.getHeight() / Rows);

		// Place the regions into a 1D array in the correct order, starting from the top 
		// left, going across first. The Animation constructor requires a 1D array.
		TextureRegion[] walkFrames = new TextureRegion[Cols * Rows];
		int index = 0;
		for (int i = 0; i < Rows; i++) {
			for (int j = 0; j < Cols; j++) {
				walkFrames[index++] = tmp[i][j];
			}
		}

		return walkFrames;
	}
	
	public void DrawAnimatedTile(SpriteBatch buffer, Animation<TextureRegion> animation_, int x, int y, float stateTime) {
		// Get current frame of animation for the current stateTime
		TextureRegion currentFrame = animation_.getKeyFrame(stateTime, true);
		
		buffer.draw(currentFrame, x, y);
	}
	
	public void DrawSplash(SpriteBatch buffer, int ID, int x, int y, float x2, float y2, boolean centered) { // The x2 and y2 is Percentage of 100
			int SizeX = Images[ID].getWidth();
			int SizeY = Images[ID].getHeight();
		
			if (centered) {
				buffer.draw(Images[ID], x-((SizeX * x2)/2), y-((SizeY * y2)/2), SizeX * x2, SizeY * y2);	
			}
			else {
				buffer.draw(Images[ID], x, y, SizeX * x2, SizeY * y2);	
			}
			
	}
	
	public void DrawAny(SpriteBatch buffer, int ID, String Type, int x, int y) {
		if(Type.equals("Tiles")) {
			buffer.draw(Tiles[ID], x, y);
		}
		if(Type.equals("Images")) {
			buffer.draw(Images[ID], x, y);	
		}
	}
	
	public void DrawBackground(SpriteBatch buffer, int x, int y) {
		//Function is responsible for drawing the backgrounds, behind the tiles
		buffer.draw(Tiles[07], 0, 0, x, y);
	}
	
	public void DrawChunkDebugLines(SpriteBatch buffer, int x, int y , int TileSize, int cameraX, int cameraY) {
		//buffer.drawLine(0-cameraX, 0-cameraY, (TileSize*16)-cameraX, 0-cameraY);
		//buffer.drawLine(0-cameraX, 0- cameraY, 0-cameraX, (TileSize*16)-cameraY);
		//buffer.drawLine((TileSize*16)-cameraX, 0-cameraY, (TileSize*16)-cameraX, ((TileSize*16))-cameraY);
		//buffer.drawLine(0-cameraX, ((TileSize*16))-cameraY, (TileSize*16)-cameraX, ((TileSize*16))-cameraY);
	}
	
	//Renders the tiles across the world
	public void DrawTiles(SpriteBatch buffer, int OffsetX, int OffsetY, int TileSize, int WorldSize) {
	//Function is for drawing the main tiles
		
		
		
		for(int i=0; i < 60; ++i){  //draws the top layer of grass
			buffer.draw(Tiles[01], i*40 - OffsetX, 20 - OffsetY, TileSize, TileSize);
		}
		for(int i=0; i < 260; ++i){  //draws the dirt
			if (i < 60) {
				buffer.draw(Tiles[00], i*40 - OffsetX, 60 - OffsetY, TileSize, TileSize);
			}
			else if (i >= 60 && i < 120){
				buffer.draw(Tiles[00], (i - 60)*40 - OffsetX, 100 - OffsetY, TileSize, TileSize);
			}
			else if (i >= 120 && i < 200){ 
				buffer.draw(Tiles[00], (i - 120)*40 - OffsetX, 140 - OffsetY, TileSize, TileSize);
			}
			else if (i >= 200 && i < 260){ 
				buffer.draw(Tiles[00], (i - 200)*40 - OffsetX, 180 - OffsetY, TileSize, TileSize);
			}
		}
	}
	
	//This will handle the animations as well
	public void Player(SpriteBatch buffer, int PosX, int PosY, String direction) {
		if (direction.equals("up")) { //UP
			//Common.print("player moved up");
			buffer.draw(Tiles[53], PosX, PosY);
			//Common.print("player drawn at x:" + PosX + " and y:" + PosY + " at sizes " + Sizex + " " + Sizey + " .");
		}
		if (direction.equals("left")) {
			//Common.print("player moved left");
			buffer.draw(Tiles[55], PosX, PosY);
		}
		if (direction.equals("down")) {
			//Common.print("player moved down");
			buffer.draw(Tiles[54], PosX, PosY);
		}
		if (direction.equals("right")) {
			//Common.print("player moved right");
			buffer.draw(Tiles[56], PosX, PosY);
		}
	}
	
	public void GUIDrawText(SpriteBatch buffer, int PosX, int PosY, Color colour, String Text) {
		//Color temp = font.getColor();
		//font.setColor(colour);
		font.draw(buffer, Text, PosX , PosY);
		//font.setColor(temp);
	}
	
	public void HUDAchievement(SpriteBatch buffer, int PosX, int PosY, String text, int iconID, float Opacity, boolean Anim, float Time) {/*
		buffer.draw(GUI[00], PosX, PosY);
		buffer.draw(GUI[01], PosX + Tiles[59].getWidth(), PosY);
		buffer.draw(GUI[02], PosX + (Tiles[59].getWidth()*2), PosY);
		if (Anim) {
			if (iconID == 4 || iconID == 5 || iconID == 6) {
				DrawAnimatedTile(buffer, StarsAnimation, PosX + (Tiles[59].getWidth()*2) + (Tiles[59].getWidth()/2), PosY + (Tiles[59].getWidth()/2)-(Tiles[59].getWidth()/8), Time);
			} else {
				Common.print("Not Configured To Animate");
			}
		} else {
			buffer.draw(Tiles[iconID], PosX + (Tiles[59].getWidth()*2) + (Tiles[59].getWidth()/2), PosY + (Tiles[59].getWidth()/2)-(Tiles[59].getWidth()/8), Tiles[iconID].getWidth()/4, Tiles[iconID].getHeight()/4);
		}
		//buffer.draw(Tiles[iconID], PosX, PosY);
		font.draw(buffer, text, PosX + 20, PosY + (Tiles[59].getHeight()/2)+5);
	*/}
	
	public void HUDNotification(SpriteBatch buffer, int PosX, int PosY, int size, String text, float tics) {
		//buffer.draw(GUI[00], PosX, PosY);
		//buffer.draw(GUI[01], PosX + Tiles[59].getWidth(), PosY);
		//buffer.draw(GUI[02], PosX + (Tiles[59].getWidth()*2), PosY);
		//font.draw(buffer, text, PosX + 20, PosY + (Tiles[59].getHeight()/2)+5);
		if (tics % 50 == 0) {
			font.setColor(1, 1, 1, 0);
		}
		font.draw(buffer, text, PosX, PosY, size, 0, true);
		font.setColor(Color.WHITE);
	}
	
	public void HUDFuel(SpriteBatch buffer, int PosX, int PosY, float Angle, boolean Flash) {/*
		int scale = 1;
		if (Flash) {
			buffer.draw(GUI[20], PosX, PosY);
			buffer.draw(GUI[21], PosX + Tiles[59].getWidth(), PosY);
			buffer.draw(GUI[22], PosX, PosY + Tiles[59].getWidth());
			buffer.draw(GUI[23], PosX + Tiles[59].getWidth(), PosY + Tiles[59].getWidth());
		} else {
			buffer.draw(GUI[16], PosX, PosY, Tiles[59].getWidth()*scale, Tiles[59].getHeight()*scale);
			buffer.draw(GUI[17], PosX + (Tiles[59].getWidth()*scale), PosY, Tiles[59].getWidth()*scale, Tiles[59].getHeight()*scale);
			buffer.draw(GUI[18], PosX, PosY + (Tiles[59].getHeight()*scale), Tiles[59].getWidth()*scale, Tiles[59].getHeight()*scale);
			buffer.draw(GUI[19], PosX + (Tiles[59].getWidth()*scale), PosY + (Tiles[59].getHeight()*scale), Tiles[59].getWidth()*scale, Tiles[59].getHeight()*scale);
			//buffer.draw(GUI[16], PosX, PosY);
			//buffer.draw(GUI[17], PosX + Tiles[59].getWidth(), PosY);
			//buffer.draw(GUI[18], PosX, PosY + Tiles[59].getWidth());
			//buffer.draw(GUI[19], PosX + Tiles[59].getWidth(), PosY + Tiles[59].getHeight());
		}
		buffer.draw(GUI[15], PosX, PosY, PosX+(5*scale), PosY+(5*scale), (Tiles[59].getWidth()*scale), (Tiles[59].getWidth()*scale), 1, 1, Angle, 0, 0, (Tiles[59].getWidth()*scale), (Tiles[59].getWidth()*scale), false, false);
		//buffer.draw(GUI[15], PosX, PosY, Tiles[59].getWidth()*scale, Tiles[59].getHeight()*scale);
		
	*/}
	
	public void MoneyFeedback(SpriteBatch buffer, int PosX, int PosY, String text, float DeltaTime) {
		font.setColor(Color.YELLOW);
		//font.setColor(0, 1, 1, 0.1f);
		font.draw(buffer, text  + "$", PosX + 20, PosY + 20 + (DeltaTime/2f));
		font.setColor(Color.WHITE);
	}

	public void warningText(SpriteBatch buffer, int PosX, int PosY, String text, float tics) {
		if (tics % 10 == 0) {
			font.setColor(Color.RED);
		}
		font.draw(buffer, text, PosX + 20, PosY + 20);
		font.setColor(Color.WHITE);
	}
	
	public void HUDDescr(SpriteBatch buffer, int PosX, int PosY, String text) {
		font.draw(buffer, text, PosX, PosY);
	}
	
	public void MenuBackground(SpriteBatch buffer, int PosX, int PosY, int Pos2X, int Pos2Y, float Oppacity) {
		Color col = buffer.getColor();
		buffer.setColor(new Color(0, 0, 0, Oppacity));
		buffer.draw(Tiles[07], 0, 0, PosX, PosY);
		buffer.setColor(col);
	}
	
	//Draw Calculation Methods
	public boolean GUIButtonCheck(int[] mouse, int[] button) {
		if(mouse[1] >= button[0] && mouse[1] <= button[2]) {
			if(mouse[2] >= button[1] && mouse[2] <= button[3]) { //Audio Settings
				return true;
			}
		}
		return false;
	}
	
	public boolean GUICheckBoxCheck(int[] mouse, int[] checkbox) {
		if(mouse[1] >= checkbox[0] && mouse[1] <= checkbox[2]) { // THE CHECK BOX
			if(mouse[2] >= checkbox[1] && mouse[2] <= checkbox[3]) {
				return true;
			}
		}
		return false;
	}
	
	public boolean GuiSliderCheck(int[] mouseDrag, int[] Slider) {
		if(mouseDrag[1] >= Slider[0] && mouseDrag[1] <= Slider[2]) { // THIS IS THE MasterVolume SLIDER
			if(mouseDrag[2] >= Slider[1] && mouseDrag[2] <= Slider[3]) {
				return true;
			}
		}
		return false;
	}
	
	public float GuiSliderReading(int[] mouseDrag, int[] Slider) {
		float SliderValuetemp = ((float)(mouseDrag[1] - Slider[0])/(Slider[2] - Slider[0]));
		return SliderValuetemp;
	}
	
	public ShapeRenderer debugRenderer = new ShapeRenderer();

    public void DrawDebugLine(Vector2 start, Vector2 end, int lineWidth, Color color, Matrix4 projectionMatrix)
    {
        Gdx.gl.glLineWidth(lineWidth);
        debugRenderer.setProjectionMatrix(projectionMatrix);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugRenderer.setColor(color);
        debugRenderer.line(start, end);
        debugRenderer.end();
        Gdx.gl.glLineWidth(1);
    }
    
    public void DrawDebugPoint(Vector2 start, int lineWidth, Color color, Matrix4 projectionMatrix)
    {
        Gdx.gl.glLineWidth(lineWidth);
        debugRenderer.begin(ShapeRenderer.ShapeType.Filled);
        debugRenderer.circle(start.x, start.y, lineWidth);
        debugRenderer.setColor(color);
        debugRenderer.end();
        Gdx.gl.glLineWidth(1);
    }
	
}