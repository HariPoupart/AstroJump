package com.example.astrojumppseudocode;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;

public class AstroJump extends Application {

    //gameloop method propreties
    private static final int TARGET_FPS = 60;
    private static final long NANOSECONDS_PER_FRAME = 1_000_000_000 / TARGET_FPS;
    private long lastUpdateTime = 0;
    private long lastUpdateTimePlanetTimer = 0;
    public int screenHight;
    public int screenWidth;
    public static BooleanProperty startLoopListener = new SimpleBooleanProperty(false);


    //game objects
    private Player player;
    private Obstacle[] obstacles;
    private Star[] star;
    private ArrayList<Net> nets;
    protected static ArrayList<Planet> planetArray;
    public Planet currentPlanet;

    //game pane propreties
    private static final int GROUND_Y = 200;

    public static void main(String[] args) {
        launch(args);
        //initiate planetArray
        Planet mercury = new Planet(-10f,30f,30f);
        Planet venus = new Planet(-90f,30f,30f);
        Planet earth = new Planet(-100f,30f,30f);
        Planet mars = new Planet(-10f,30f,30f);
        Planet jupiter = new Planet(-160f,30f,30f);
        Planet saturn = new Planet(-95f,30f,30f);
        Planet uranus = new Planet(-80f,30f,30f);
        Planet neptune = new Planet(-115f,30f,30f);
        planetArray.add(mercury);
        planetArray.add(venus);
        planetArray.add(earth);
        planetArray.add(mars);
        planetArray.add(jupiter);
        planetArray.add(saturn);
        planetArray.add(uranus);
        planetArray.add(neptune);
    }

    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("AstroJump");

        //listener
        startLoopListener.addListener(e -> {
            startGameLoop(primaryStage);
            startLoopListener = new SimpleBooleanProperty(false);
        });

        //load menuGUI
        FXMLLoader loader = new FXMLLoader(AstroJump.class.getResource("astroJumpMenu.fxml"));
        Scene scene = new Scene(loader.load(), 1366,768);
        primaryStage.setScene(scene);
        primaryStage.show();

        //listener to start game loop (called from start event handler from MenuController class)
        startLoopListener.addListener(e -> {
            startGameLoop(primaryStage);
            startLoopListener = new SimpleBooleanProperty(false);
        });
    }

    //create animation timer which calls the update method
    protected void startGameLoop(Stage primaryStage) {
        //create game scene
        Scene game = new Scene(new Group(player.getImage()),1366,768);

        //jump event handler TO DO: EVELYNE TAKE CARE OF THIS :)
        game.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE&&!player.getIsJumping()) {
                player.setIsJumping(true);
                player.setY(GROUND_Y-1);
            }
        });

        //show game scene
        primaryStage.setScene(game);
        primaryStage.show();

        //create timer to switch planet
        planetChange(System.currentTimeMillis());

        //animation timer to update character
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Calculate the time elapsed since the last frame
                if (lastUpdateTime > 0) {
                    long elapsedTime = now - lastUpdateTime;
                    // If enough time has passed, call update and save lastUpdateTime
                    if (elapsedTime >= NANOSECONDS_PER_FRAME) {
                        update(elapsedTime / 1_000_000_000.0); // Convert nanoseconds to seconds
                        lastUpdateTime = now;
                    }
                } else {
                    lastUpdateTime = now;
                }
            }
        }.start();
    }
    private void update(double deltaTime) {
        // deltaTime is the time elapsed since the last frame in seconds
        //you can multiply a value of speed or position by deltaTime to make it pixels/second
        //example:

        if(player.getIsJumping()){
            playerJump(-120f); //TASK: EVELYNE: change -9.8 to planets gravity
        }
        player.updateIsOnGround(GROUND_Y);

    }

    private void createPlayer(){
        final Image IMAGE = new Image("playerSpriteSheet.png");
        //number of columns in the spriteSheet
        final int COLUMNS = 5;
        final int STARTING_ROW = Player.RUN;
        //beginning offset
        final int OFFSET_X = 0;
        final int OFFSET_Y = 0;
        //size of one image
        final int WIDTH = 32;
        final int HEIGHT = 32;
        ImageView playerIV = new ImageView(IMAGE);
        //set player imageView to first image
        playerIV.setViewport(new Rectangle2D(OFFSET_X, OFFSET_Y, WIDTH, HEIGHT));

        //animation player imageview
        final Duration PLAYER_ANIM_DURATION= Duration.millis(1000);
        Sprite playerAnimation = new Sprite(
                playerIV,
                PLAYER_ANIM_DURATION,
                STARTING_ROW, COLUMNS,
                OFFSET_X, OFFSET_Y,
                WIDTH, HEIGHT
        );
        playerAnimation.setCycleCount(Animation.INDEFINITE);
        playerAnimation.play();

        //create player object
        player = new Player(playerIV,playerAnimation);
        player.setAnimationState(Player.RUN);
        player.setY(GROUND_Y);
    }
    private void playerJump(float gravitationalForce){
        //set player animation to jump
        player.setAnimationState(Player.JUMP); // TO DO: move to action event

        //move player
        double timeElapsed = player.getJumpTimeElapsed();
        double baseDisplacement = timeElapsed*player.getINITIAL_JUMP_SPEED();
        double acceleratedDisplacement = -0.5*gravitationalForce*Math.pow(timeElapsed,2);
        player.setY(GROUND_Y-player.getHeight()+baseDisplacement+acceleratedDisplacement);

        //if the player is back on the floor set is jumping to false
        if(player.getY()>=(GROUND_Y+player.getHeight()))
          player.setIsJumping(false);
    }

    private void planetChange(long now) {
        long threshhold = 45000; //represents time between planets

        while(true){
             now = System.currentTimeMillis();
                // Calculate the time elapsed since the last frame
                if (lastUpdateTimePlanetTimer > 0) {
                    long elapsedTime = now - lastUpdateTimePlanetTimer;
                    // If enough time has passed, call update and save lastUpdateTime
                    if (elapsedTime >= threshhold) {
                        currentPlanet = planetArray.get((int) (Math.random() * 10));
                        lastUpdateTimePlanetTimer = now;
                    }
                } else {
                    lastUpdateTimePlanetTimer = now;
                }

        }
    }

}

