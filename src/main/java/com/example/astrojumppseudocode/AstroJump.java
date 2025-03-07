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
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AstroJump extends Application {

    //gameloop method propreties
    private static final int TARGET_FPS = 60;
    private static final long NANOSECONDS_PER_FRAME = 1_000_000_000 / TARGET_FPS;
    private long lastUpdateTime = 0;
    private long lastUpdateTimePlanetTimer = 0;
    public int screenHeight = 500;
    public int screenWidth = 1000;
    public int currentPlanetInt = -1;
    public static BooleanProperty startLoopListener = new SimpleBooleanProperty(false);
    public static BooleanProperty tutorialListener = new SimpleBooleanProperty(false);
    public static BooleanProperty settingsListener = new SimpleBooleanProperty(false);


    //GAME OBJECTS
    private Group gameObjects;

    //player
    private Player player;

    //obstacles
    private ArrayList<Obstacle> obstacles = new ArrayList<>();
    private final int SPIKE_WIDTH = 42;
    private final int SPIKE_HEIGHT = 64;

    //star
    private Star star;

    //net
    private ArrayList<Net> nets = new ArrayList<>();
    private final int NET_WIDTH = 32;

    //planets
    protected static ArrayList<Planet> planetArray;

    //game pane properties
    private static final int GROUND_Y = 450;

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
        planetArray = new ArrayList<>();
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

        //listeners for action events in MenuController
        startLoopListener.addListener(e -> {
            startGameLoop(primaryStage);
            startLoopListener = new SimpleBooleanProperty(false);
        });
        tutorialListener.addListener(e -> {
            showTutorial(primaryStage);
            tutorialListener = new SimpleBooleanProperty(false);
        });
        settingsListener.addListener(e -> {
            showSettings(primaryStage);
            settingsListener = new SimpleBooleanProperty(false);
        });

        //link to FXML file
        FXMLLoader loader = new FXMLLoader(AstroJump.class.getResource("astroJumpMenu.fxml"));

        //music from Menu
        Media media;
        MediaPlayer med;
        MediaView mv;
        Pane pane = new Pane();
        try{
            File file = new File("MenuMusic.mp3");
            media = new Media(file.toURI().toString());
            med = new MediaPlayer(media);
            mv = new MediaView();
            pane.getChildren().add(mv);
            mv.setMediaPlayer(med);
            //med.play();

        }

        catch(NullPointerException e) {
            System.out.print("error NULL");
        }
        catch(Exception e) {
            System.out.print(e);
        }

        //scene
        StackPane generalPane = new StackPane(pane, loader.load());

        Scene scene = new Scene(generalPane,screenWidth,screenHeight);

        //create player
        createPlayer();


        primaryStage.setScene(scene);
        primaryStage.show();


    }
    protected void showTutorial(Stage primaryStage) {
        Scene scene = new Scene(new Pane(new ImageView("tutorial.bmp")),screenWidth,screenHeight);
        primaryStage.setScene(scene);
        primaryStage.show();

        //add listener to go back to main menu
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                try {
                    start(primaryStage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    protected void showSettings(Stage primaryStage) {
        Scene scene = new Scene(new Pane(new ImageView("tutorial.bmp")),screenWidth,screenHeight);
        primaryStage.setScene(scene);
        primaryStage.show();
//add listener
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                try {
                    start(primaryStage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    protected void startGameLoop(Stage primaryStage) {

        //game scene setup
        gameObjects = new Group(player.getImage());
        Scene game = new Scene(gameObjects,screenWidth,screenHeight);

        //jump event handler TO DO: EVELYNE TAKE CARE OF THIS :)
        game.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE&&!player.getIsJumping()) {
                player.setIsJumping(true);
                player.setY(GROUND_Y-1);
            }
        });
        // when mouse is clicked create a net
        game.setOnMouseClicked(event -> {
            createNet(event.getX(),event.getY(),-160,400);
        });

        primaryStage.setScene(game);
        primaryStage.show();

        //create and animationTimer to call to update method
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

        //update player jump
        if(player.getIsJumping()){
            playerJump(-1200,-900); //TASK: EVELYNE: change values depending on planet
        }
        //update is on ground boolean
        player.updateIsOnGround(GROUND_Y);

        //net updates
        for(int i =0; i<nets.size();i++){
            Net net = nets.get(i);

            //update position
            net.updatePosition();

            //check for collision with a star
            if(star!=null){
                if(net.isCollidingWith(star.getImage())){
                    //TO DO: code star-net interaction
                }
            }
            //check for collision with ground
            if(net.getY()+net.getHeight()>=GROUND_Y){
                //delete game object
                gameObjects.getChildren().remove(net.getImage());
                nets.remove(net);
            }
        }

        //obstacle updates
        for(int i =0;i<obstacles.size();i++){
            Obstacle obstacle = obstacles.get(i);

            //update position
            obstacle.updatePosition(deltaTime);

            //check for collisions with player
            if(obstacle.isCollidingWith(player.getImage()))
                System.out.print("GAME OVER!");//TO DO: IMPLEMENT GAME OVER
        }
    }

    //PLAYER METHODS
    private void createPlayer(){
        final Image IMAGE = new Image("playerSpriteSheet.png");
        //number of columns in the spriteSheet
        final int COLUMNS = 5;
        final int STARTING_ROW = Player.RUN;
        //beginning offset
        final int OFFSET_X = 0;
        final int OFFSET_Y = 0;
        //size of one image
        final int SPRITE_WIDTH = 32;
        final int SPRITE_HEIGHT = 32;
        final int IMAGE_WIDTH=100;
        final int IMAGE_HEIGHT=100;
        ImageView playerIV = new ImageView(IMAGE);
        //set player imageView to first image
        playerIV.setViewport(new Rectangle2D(OFFSET_X, OFFSET_Y, SPRITE_WIDTH, SPRITE_HEIGHT));
        playerIV.setFitWidth(IMAGE_WIDTH);
        playerIV.setFitHeight(IMAGE_HEIGHT);

        //animation player imageview
        final Duration PLAYER_ANIM_DURATION= Duration.millis(1000);
        Sprite playerAnimation = new Sprite(
                playerIV,
                PLAYER_ANIM_DURATION,
                STARTING_ROW, COLUMNS,
                OFFSET_X, OFFSET_Y,
                SPRITE_WIDTH, SPRITE_HEIGHT
        );
        playerAnimation.setCycleCount(Animation.INDEFINITE);
        playerAnimation.play();

        //create player object

        player = new Player(playerIV,playerAnimation);
        player.setAnimationState(Player.RUN);
        player.setY(GROUND_Y-player.getHeight());
        player.setX(0);

    }
    private void playerJump(float gravitationalForce,float initialJumpSpeed){
        //set player animation to jump
        player.setAnimationState(Player.JUMP); // TO DO: move to action event

        //move player
        double timeElapsed = player.getJumpTimeElapsed();
        double baseDisplacement = timeElapsed*initialJumpSpeed;
        double acceleratedDisplacement = -0.5*gravitationalForce*Math.pow(timeElapsed,2);

        player.setY(GROUND_Y-player.getHeight()+baseDisplacement+acceleratedDisplacement);

        //if the player is back on the floor set is jumping to false
        if(player.getY()>=(GROUND_Y-player.getHeight())){
            player.setIsJumping(false);
            //adjust player y values to ground
            player.setY(GROUND_Y-player.getHeight());
        }
    }

    //NET METHOD
    public void createNet(double mouseX,double mouseY,float gravity, float netForce){
        //calculate the inital position of the net
        double initialPosX = player.getX()+player.getWidth();
        double initialPosY = player.getY()+(0.5*player.getHeight())-(0.5*NET_WIDTH);

        //calculate the angle of the throw
        double angle = Math.atan((mouseY-initialPosY)/(mouseX-initialPosX));

        //calculate the speed in each axis
        double initialSpeedX = netForce* Math.cos(angle);
        double initialSpeedY = netForce* Math.sin(angle);

        //create net
        //TO DO: ADD WIND RESISTANCE DURING STORM
        //adds a new net to the nets array
        nets.add(new Net(new ImageView("Net.png"),initialPosX,initialPosY,initialSpeedX,initialSpeedY,gravity,0));
        //adds the last net added to nets to the game object group
        gameObjects.getChildren().add(nets.getLast().getImage());
        //set its initial Positions to the nets initial positions
        nets.getLast().setX(initialPosX);
        nets.getLast().setY(initialPosY);

        currentPlanetInt=0;
        createSpike(-100f,0f);

    }

    //OBSTACLE METHODS
    public void createSpike(float speedX, float speedY){
        //add new obstacle
        obstacles.add(new Obstacle(new ImageView("Obstacle3.png"),SPIKE_WIDTH,SPIKE_HEIGHT,speedX,speedY));

        //change image view
        ImageView imgV = obstacles.getLast().getImage();
        Obstacle obstacle = obstacles.getLast();

        //set to the right spike image (random spike on the current planet)
        imgV.setViewport(new Rectangle2D((int)(Math.random()*6),currentPlanetInt*32,21,32));

        //add imageView to game objects
        gameObjects.getChildren().add(imgV);

        //set obstacle to the right position
        obstacle.setY(GROUND_Y-obstacle.getHeight());
        obstacle.setX(screenWidth);//CHECK

    }

    //PLANET METHOD
    private void planetChange() {
        long threshhold = 45000; //represents time between planets in milliseconds

        while(true){
            long now = System.currentTimeMillis();
            // Calculate the time elapsed since the last frame
            if (lastUpdateTimePlanetTimer > 0) {
                long elapsedTime = now - lastUpdateTimePlanetTimer;
                // If enough time has passed, call update and save lastUpdateTime
                if (elapsedTime >= threshhold) {
                    currentPlanetInt = (int) (Math.random() * 8);
                    lastUpdateTimePlanetTimer = now;
                }
            } else {
                lastUpdateTimePlanetTimer = now;
            }

        }
    }

    //MUSIC METHODS
//    protected void musicPlayer() {
//        Media music = new Media (planetArray.get(currentPlanetInt) + ".mp3");
//        MediaPlayer player = new MediaPlayer(music);
//        player.play();
//    }


}