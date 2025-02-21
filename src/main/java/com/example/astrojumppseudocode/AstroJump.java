package com.example.astrojumppseudocode;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AstroJump extends Application {

    //gameloop method propreties
    private static final int TARGET_FPS = 60;
    private static final long NANOSECONDS_PER_FRAME = 1_000_000_000 / TARGET_FPS;
    private long lastUpdateTime = 0;

    //game objects
    Player player;


    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("AstroJump");

        //create player object and imageview
        createPlayer();

        //show the stage
        primaryStage.setScene(new Scene(new Group(player.getImage())));
        primaryStage.show();

        // Start the game loop
        startGameLoop();

    }
    private void startGameLoop() {
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
        player.setX(player.getX()+10*deltaTime);

    }

    private void createPlayer(){
        final Image IMAGE = new Image("The_Horse_in_Motion.jpg");
        //number of columns in the spriteSheet
        final int COLUMNS = 4;
        final int STARTING_ROW = Player.RUN;
        //beginning offset
        final int OFFSET_X = 18;
        final int OFFSET_Y = 25;
        //size of one image
        final int WIDTH = 374;
        final int HEIGHT = 243;
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
    }

}