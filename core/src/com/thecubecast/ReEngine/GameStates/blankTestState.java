// GameState that shows logo.

package com.thecubecast.ReEngine.GameStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.thecubecast.ReEngine.Data.Common;
import com.thecubecast.ReEngine.Data.GameStateManager;
import com.thecubecast.ReEngine.Data.controlerManager;

import java.util.Random;

import static com.badlogic.gdx.graphics.GL20.GL_NEAREST;

public class blankTestState extends GameState {

    Boolean Started = false;

    public Random rnd;

    public int Fishies = 0;
    public int ScoreValue;
    public float TimeRemainingValue;

    public boolean Baited;
    public float Progress;
    private Fish FishAttempt;

    public enum Fish {

        BigMouthBass(7, 6, 0.009f),
        Trout(4, 3, 0.005f),
        Sunfish(2, 1, 0.0004f),
        Catfish(3, 4, 0.006f),
        Pike(6, 5, 0.007f);

        int timeBoost;
        int ScoreBoost;
        float Difficulty;
        Fish(int timeB, int ScoreB, float Difficult) {
            timeBoost = timeB;
            ScoreBoost = ScoreB;
            Difficulty = Difficult;
        }
    }

    Texture Window;
    Texture Background;

    private Skin skin;
    private Stage Screen;

    private Table TopLeft;
    Label Caught;
    Label Score;
    Label TimeRemaining;

    private Table TopTopRight;
    private Table Center;
    private Table Bottom;
        TextButton Bait;
        TextButton Reel;
        TextButton Cast;
    private Table Menu;
        Label Yourfish;
        Label YourScore;

    private ProgressBar FishingBar;
    Label FishingLabel;
    Label BaitLabel;

    public blankTestState(GameStateManager gsm) {
        super(gsm);
    }

    public void init() {

        gsm.DiscordManager.setPresenceDetails("LibGDX Jam 1 | Three Button Fishing");
        gsm.DiscordManager.setPresenceState("Setting up their tackle box!");

        rnd = new Random();
        skin = new Skin(Gdx.files.internal("Skins/ThreeColors/new.json"));

        Screen = new Stage(new FitViewport(gsm.Width, gsm.Height));
        Gdx.input.setInputProcessor(Screen);

        Bottom = new Table();
        Bottom.setFillParent(true);
        Bottom.bottom();
        Screen.addActor(Bottom);

        TopLeft = new Table();
        TopLeft.setFillParent(true);
        TopLeft.top().left();
        Screen.addActor(TopLeft);

        Center = new Table();
        Center.setFillParent(true);
        Center.center();
        Screen.addActor(Center);

        TopTopRight = new Table();
        TopTopRight.setFillParent(true);
        TopTopRight.top().right();
        Screen.addActor(TopTopRight);

        Menu = new Table();
        Menu.setFillParent(true);
        Menu.center();
        Menu.setVisible(false);
        Screen.addActor(Menu);

        //TOP LEFT

        Caught = new Label("Fish Caught: " + Fishies, skin);
        Caught.setColor(0, 0, 0, 1);
        TopLeft.add(Caught);
        TopLeft.row();
        Score = new Label("Score: ", skin);
        Score.setColor(0, 0, 0, 1);
        TopLeft.add(Score);
        TopLeft.row();
        TimeRemaining = new Label("Time Remaining: ", skin);
        TimeRemaining.setColor(0, 0, 0, 1);
        TopTopRight.add(TimeRemaining);
        TopTopRight.row();

        //Menu

        Label Title = new Label("Congratulations!", skin);
        Menu.add(Title).row();

        Label gap = new Label("", skin);
        Menu.add(gap).row();

        Yourfish = new Label("You caught " + Fishies + " fish!", skin);
        Menu.add(Yourfish).row();
        YourScore = new Label("Your Score was " + ScoreValue + "!", skin);
        Menu.add(YourScore).row();

        TextButton Reset = new TextButton("Play again?", skin);
        Reset.padLeft(20).padRight(20);
        Menu.add(Reset).row();
        Label subText = new Label("Press (Y) or Enter", skin);
        Menu.add(subText);

        Reset.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                ResetThree();
            }
        });

        //Center
        FishingBar = new ProgressBar(0f, 1f, 0.01f, true, skin);
        Center.add(FishingBar);
        Center.row().row();
        FishingLabel = new Label("Try to catch a fish!!", skin);
        Center.add(FishingLabel);
        Center.row();
        BaitLabel = new Label("Baited: " + Baited, skin);
        Center.add(BaitLabel);

        //BOTTOM

        Bait = new TextButton("Add Bait (X)", skin);
        Bottom.add(Bait).pad(12);

        Reel = new TextButton("Reel In (Z)", skin);
        Bottom.add(Reel).pad(12);

        Cast = new TextButton("Cast (C)", skin);
        Bottom.add(Cast);

        Bait.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                baitHook();
            }
        });

        Reel.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                reelIn();
            }
        });

        Cast.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                castLine();
            }
        });

        Background = new Texture(Gdx.files.internal("background.png"));
        Window = new Texture(Gdx.files.internal("Window.png"));
    }

    public void update() {

        handleInput();

        if (Started && TimeRemainingValue == 0) {
            FishingLabel.setText("Time's Up!");

            gsm.DiscordManager.setPresenceState("Times up!");
            gsm.DiscordManager.getPresence().endTimestamp = System.currentTimeMillis() / 1000;

            BaitLabel.setText("");
            Yourfish.setText("You caught " + Fishies + " fish!");
            YourScore.setText("Your Score was " + ScoreValue + "!");


            Menu.setVisible(true);


        } else {

            Menu.setVisible(false);

            if(TimeRemainingValue >= 0)
                TimeRemainingValue -= Gdx.graphics.getDeltaTime();
            if (TimeRemainingValue < 0)
                TimeRemainingValue = 0;

            if (gsm.ctm.controllers.size() > 0) {
                //Controller connected
                Bait.setText("Add Bait (X)");
                Reel.setText("Reel In (A)");
                Cast.setText("Cast (B)");
            } else {
                Bait.setText("Add Bait (X)");
                Reel.setText("Reel In (Z)");
                Cast.setText("Cast (C)");
            }

            if (Progress >= 0.02) {
                if (FishAttempt != null)
                    Progress -= FishAttempt.Difficulty - (Fishies / 100);
            }

            FishingBar.setValue(Progress);

            //Update Text
            Caught.setText("Fish Caught: " + Fishies);
            Score.setText("Score: " + ScoreValue);
            String temp = String.format ("%.2f", TimeRemainingValue);
            TimeRemaining.setText("Time Remaining: " + temp);


            if (FishAttempt != null) {
                FishingLabel.setText("Attempting to catch " + FishAttempt.name());
                BaitLabel.setText("");
            } else {
                if (Baited == true)
                    FishingLabel.setText("Cast your line!");
                else
                    FishingLabel.setText("Bait your hook!");

                BaitLabel.setText("Baited: " + Baited);
            }
        }

        Screen.act();
    }

    public void draw(SpriteBatch g, int width, int height, float Time) {
        g.begin();
        Gdx.gl.glClearColor(255f, 255f, 255f, 1);

        g.draw(Background, 0, 0, height, width);
        if (Started && TimeRemainingValue == 0) {
            Center.setVisible(false);
            g.draw(Window , 80, 80 , height - 140, width - 140);
        } else {
            Center.setVisible(true);
        }
        Screen.getRoot().draw(g, 1f);
        Screen.setDebugAll(false);

        g.end();
    }

    public void handleInput() {

        if (Gdx.input.isKeyJustPressed(Keys.X) || gsm.ctm.isButtonJustDown(0, controlerManager.buttons.BUTTON_X)) {
            baitHook();
        }

        if (Gdx.input.isKeyJustPressed(Keys.C) || gsm.ctm.isButtonJustDown(0, controlerManager.buttons.BUTTON_B)) {
            castLine();
        }

        if (Gdx.input.isKeyJustPressed(Keys.Z) || gsm.ctm.isButtonJustDown(0, controlerManager.buttons.BUTTON_A)) {
            reelIn();
        }

        if (Started && TimeRemainingValue == 0) {
            if (Gdx.input.isKeyJustPressed(Keys.ENTER) || gsm.ctm.isButtonJustDown(0, controlerManager.buttons.BUTTON_Y)) {
                ResetThree();
            }
        }
    }

    public void baitHook() {
        if(FishAttempt == null)
            Baited = true;
    }

    public void castLine() {
        Common.print("Casted Line");
        if (Baited) {
            if (!Started) {
                TimeRemainingValue = 30;
                Started = true;
                gsm.DiscordManager.setPresenceState("Fishing!");
                gsm.DiscordManager.getPresence().startTimestamp = System.currentTimeMillis() / 1000;
            }
            Baited = false;
            FishAttempt = Fish.values()[rnd.nextInt(Fish.values().length)];
        }

    }

    public void reelIn() {
        if (FishAttempt != null)
            Progress += 0.1f;

        if (Progress >= 1) {
            CatchFish();
        }
    }

    public void CatchFish() {
        Progress = 0.3f;
        Common.print("Caught Fish");
        //Add to fish total
        Fishies += 1;
        //Add points
        ScoreValue += FishAttempt.ScoreBoost;
        TimeRemainingValue += FishAttempt.timeBoost;


        Baited = false;
        FishAttempt = null;
    }

    private void ResetThree() {
        Progress = 0.0f;
        //Add to fish total
        Fishies = 0;
        //Add points
        ScoreValue = 0;
        TimeRemainingValue = 0;


        Baited = false;
        FishAttempt = null;
        Started = false;
    }

}