package com.example.astrojumppseudocode;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AstroJump extends Application {

    //gameloop method propreties
    private static final int TARGET_FPS = 60;
    private static final long NANOSECONDS_PER_FRAME = 1_000_000_000 / TARGET_FPS;
    private long lastUpdateMethodTime = 0;
    private Timeline levelChanger;

    private float objectSpeed;
    private long score = 0;
    private StringBuilder planetsDiscovered = new StringBuilder("00000000");
    private long obstacleSpawnIntervalNano = (long)2*1_000_000_000;
    private long spawnIntervalDecrement = 5_000;
    private long lastObstacleSpawnTime =0;
    private long lastStarSpawnTime = 0;
    private long starSpawnIntervalNano= (long)30*1_000_000_000;
    public int screenHeight = 500;
    public int screenWidth = 1000;

    public int currentPlanetInt = 0;

    public static BooleanProperty startLoopListener = new SimpleBooleanProperty(false);
    public static BooleanProperty tutorialListener = new SimpleBooleanProperty(false);
    public static BooleanProperty settingsListener = new SimpleBooleanProperty(false);
    public static boolean stopAnimationTimer;

    public MediaPlayer mediaPlayer;

    //GAME OBJECTS
    private Group gameObjects;

    //player
    private Player player;

    //obstacles
    private ArrayList<SimpleMovingImage> obstacles = new ArrayList<>();

    //star
    private Star star;

    //net
    private ArrayList<Net> nets = new ArrayList<>();

    //Background
    Background background;
    //planets
    protected static ArrayList<Planet> planetArray;

    //game pane properties
    private static final int GROUND_Y = 390;

    public static void main(String[] args) {
        //initiate planetArray with gravities from NSSDC
        Planet mercury = new Planet("mercury", -567f,-600f,300f);
        Planet venus = new Planet("venus",-1382f,-900f,300f);
        Planet earth = new Planet("earth",-1524f,-1000f,300f);
        Planet mars = new Planet("mars",-574f,-600f,300f);
        Planet jupiter = new Planet("jupiter",-3596f,-1500f,300f);
        Planet saturn = new Planet("saturn",-1396f,-900f,300f);
        Planet uranus = new Planet("uranus",-1355f,-1000f,300f);
        Planet neptune = new Planet("neptune",-1707f,-1000f,300f);
        planetArray = new ArrayList<>();
        planetArray.add(mercury);
        planetArray.add(venus);
        planetArray.add(earth);
        planetArray.add(mars);
        planetArray.add(jupiter);
        planetArray.add(saturn);
        planetArray.add(uranus);
        planetArray.add(neptune);
        //launch args
        launch(args);
    }

    public void start(Stage primaryStage) throws IOException {
        BorderPane borderPane = new BorderPane();

        // High score and total stars collected
        VBox vBox1 = new VBox();
        vBox1.setAlignment(Pos.CENTER);
        vBox1.setSpacing(20);
        vBox1.setPadding(new Insets(50, 0, 0, 150));
        Label lbScore = new Label("High Score:");
        TextField tfScore = new TextField();
        tfScore.setEditable(false);
        Label lbStars = new Label("Total Stars Collected:");
        TextField tfStars = new TextField();
        tfStars.setEditable(false);
        vBox1.getChildren().addAll(lbScore, tfScore, lbStars, tfStars);
        borderPane.setLeft(vBox1);

        // Start, tutorial, settings and exit
        VBox vBox2 = new VBox();
        vBox2.setAlignment(Pos.CENTER);
        vBox2.setSpacing(20);
        vBox2.setPadding(new Insets(50,0,0,0));

        // Start button
        Button btStart = new Button("Start");
        btStart.setPrefWidth(150);
        btStart.setPrefHeight(30);

        // Tutorial button
        Button btTutorial = new Button("Tutorial");
        btTutorial.setPrefWidth(150);
        btTutorial.setPrefHeight(30);

        // Settings button
        Button btSettings = new Button("Settings");
        btSettings.setPrefWidth(150);
        btSettings.setPrefHeight(30);

        // Exit button
        Button btExit = new Button("Exit");
        btExit.setPrefWidth(150);
        btExit.setPrefHeight(30);
        vBox2.getChildren().addAll(btStart, btTutorial, btSettings, btExit);
        borderPane.setCenter(vBox2);

        // Planets discovered
        VBox vBox3 = new VBox();
        vBox3.setAlignment(Pos.CENTER);
        vBox3.setSpacing(20);
        vBox3.setPadding(new Insets(50, 150, 0, 0));
        Label lbPlanets = new Label("Planets Discovered:");
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Mercury Image
        ImageView mercuryImage = new ImageView("file:Mercury.png");
        mercuryImage.setFitWidth(30);
        mercuryImage.setFitHeight(30);
        gridPane.add(mercuryImage, 0, 0);

        // Venus Image
        ImageView venusImage = new ImageView("file:Venus.png");
        venusImage.setFitWidth(30);
        venusImage.setFitHeight(30);
        gridPane.add(venusImage, 1, 0);

        // Earth Image
        ImageView earthImage = new ImageView("file:Earth.png");
        earthImage.setFitWidth(30);
        earthImage.setFitHeight(30);
        gridPane.add(earthImage, 2, 0);

        // Mars Image
        ImageView marsImage = new ImageView("file:Mars.png");
        marsImage.setFitWidth(30);
        marsImage.setFitHeight(30);
        gridPane.add(marsImage, 3, 0);

        // Jupiter Image
        ImageView jupiterImage = new ImageView("file:Jupiter.png");
        jupiterImage.setFitWidth(30);
        jupiterImage.setFitHeight(30);
        gridPane.add(jupiterImage, 0, 1);

        // Saturn Image
        ImageView saturnImage = new ImageView("file:Saturn.png");
        saturnImage.setFitWidth(30);
        saturnImage.setFitHeight(30);
        gridPane.add(saturnImage, 1, 1);

        // Uranus Image
        ImageView uranusImage = new ImageView("file:Uranus.png");
        uranusImage.setFitWidth(30);
        uranusImage.setFitHeight(30);
        gridPane.add(uranusImage, 2, 1);

        // Neptune Image
        ImageView neptuneImage = new ImageView("file:Neptune.png");
        neptuneImage.setFitWidth(30);
        neptuneImage.setFitHeight(30);
        gridPane.add(neptuneImage, 3, 1);

        vBox3.getChildren().addAll(lbPlanets, gridPane);
        borderPane.setRight(vBox3);

        primaryStage.setTitle("AstroJump");

        //buttons action handler
                btStart.setOnAction(e -> MenuController.startButton());
                btTutorial.setOnAction(e -> MenuController.tutorialButton());
                btSettings.setOnAction(e -> MenuController.settingButton());
                btExit.setOnAction(e -> MenuController.exitButton());
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
        MediaView mv;
        Pane pane = new Pane();
        try{
            File file = new File("MenuMusic.mp3");
            media = new Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mv = new MediaView();
            pane.getChildren().add(mv);
            mv.setMediaPlayer(mediaPlayer);
            //mediaPlayer.play();

        }

        catch(NullPointerException e) {
            System.out.print("error NULL");
        }
        catch(Exception e) {
            System.out.print(e);
        }

        //scene
        StackPane generalPane = new StackPane(pane, borderPane);

        Scene scene = new Scene(generalPane,screenWidth,screenHeight);

        //create player
        createPlayer();

        //initialize star
        initializeStar();

        //create background
        createBackground();


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
    protected void showGameOverScreen(Stage primaryStage) {
        ImageView gameOverScreen = new ImageView("GameOver.bmp");
        gameOverScreen.setFitHeight(screenHeight);
        gameOverScreen.setFitWidth(screenWidth);
        Scene scene = new Scene(new Pane(gameOverScreen),screenWidth,screenHeight);
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
        objectSpeed = -500;
        //add first planet to planetsDiscovered
        if(!IOMethods.getPlanetsDiscovered().isEmpty()) {
            planetsDiscovered = IOMethods.getPlanetsDiscovered();
        }
        planetsDiscovered = IOMethods.getPlanetsDiscovered();
        //add planet to planetsDiscovered
        if(planetsDiscovered.charAt(currentPlanetInt) == '0') {
            planetsDiscovered.setCharAt(currentPlanetInt, '1');
        }
        //start planetChange method
        planetChange();
        //resetting all game variables
        stopAnimationTimer = false;

        //game scene setup
        gameObjects = new Group(background.getImage(),player.getImage(),star.getImage());
        Scene game = new Scene(gameObjects,screenWidth,screenHeight);

        //initialize the next star spawning
        randomizeStarSpawnTime();

        //event handlers on scene
        //escape event handler
        game.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
                    if (event.getCode() == KeyCode.ESCAPE) {
                        try {
                            start(primaryStage);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

        //jump event handler
        game.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE&&!player.getIsJumping()) {
                player.setIsJumping(true);
                player.setY(GROUND_Y-1);
            }
        });
        // when mouse is clicked create a net
        game.setOnMouseClicked(event -> {
            createNet(event.getX(),event.getY(),-160,400);
            //createNet(event.getX(),event.getY(),planetArray.get(currentPlanetInt).getGravity(),400);
        });

        primaryStage.setScene(game);
        primaryStage.show();

        //create and animationTimer to call to update method and gameObjectSpawner
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Calculate the time elapsed since the last frame
                if(stopAnimationTimer) {
                    System.out.println("STOPPING ANIMATION");
                this.stop();
                }
                if (lastUpdateMethodTime > 0) {
                    long elapsedTime = now - lastUpdateMethodTime;
                    // If enough time has passed, call update and save lastUpdateTime
                    if (elapsedTime >= NANOSECONDS_PER_FRAME) {
                        //update score
                        score += (-0.0000000001) * elapsedTime * objectSpeed;
                        update(elapsedTime / 1_000_000_000.0, primaryStage); // Convert nanoseconds to seconds
                        gameObjectSpawner(now);
                        lastUpdateMethodTime = now;
                    }
                } else {
                    lastUpdateMethodTime = now;
                }
            }
        }.start();
    }
    private void update(double deltaTime, Stage stage) {
        // deltaTime is the time elapsed since the last frame in seconds
        //you can multiply a value of speed or position by deltaTime to make it pixels/second
        //increase object speeds
        objectSpeed += (float) (objectSpeed*0.01*deltaTime);
        updateGameObjectsSpeed();


        //update PLAYER jump
        if(player.getIsJumping()){
            playerJump(planetArray.get(currentPlanetInt).getGravity(),planetArray.get(currentPlanetInt).getjumpForce());
        }
        //update is on ground boolean
        player.updateIsOnGround(GROUND_Y);

        //NET updates
        for(int i =0; i<nets.size();i++){
            Net net = nets.get(i);

            //update position
            net.updatePosition();

            //check for collision with a star
            if(star!=null){
                if(net.isCollidingWith(star.getImage())){
                    player.addOneStar();
                    //modify score

                    //reset star
                    spawnStar(screenWidth,0,0,0,0);
                }
            }
            //check for collision with ground
            if(net.getY()+net.getHeight()>=GROUND_Y){
                //delete game object
                gameObjects.getChildren().remove(net.getImage());
                nets.remove(net);
            }

            //spawn obstacles
        }

        //OBSTACLE updates
        for(int i =0;i<obstacles.size();i++){
            SimpleMovingImage obstacle = obstacles.get(i);

            //update position
            obstacle.updatePosition(deltaTime);

            //check for collisions with player
            if(obstacle.isCollidingWith(player.getImage())){
                //game over methods
                IOMethods saveData = new IOMethods(score, IOMethods.getTotalStarsCollected() + player.getStarsCaught(), planetsDiscovered);
                System.out.print("GAME OVER!");
                stopAnimationTimer = true;
                showGameOverScreen(stage);
            }

            //if the obstacle is out of bounds delete it
            if(obstacle.isOutOfBounds(GROUND_Y)){
                gameObjects.getChildren().remove(obstacle.getImage());
                obstacle=null;
                obstacles.remove(i);
            }
        }

        //STAR movement and collisions
        star.updatePosition(deltaTime);

        if(star.isCollidingWith(player.getImage())){
            player.addOneStar();
            //reset star
            spawnStar(screenWidth,0,0,0,0);
        }
        //reset the star if it is out of bounds
        if(star.isOutOfBounds(GROUND_Y)){
            spawnStar(screenWidth,0,0,0,0);
        }

        //background update
        background.updatePosition(deltaTime);

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
        int NET_WIDTH = 32;
        //calculate the inital position of the net
        double initialPosX = player.getX()+player.getWidth();
        double initialPosY = player.getY()+(0.5*player.getHeight())-(0.5* NET_WIDTH);

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

    }

    //OBSTACLE METHODS
    public void createSpike(){
        //add new obstacle
        int SPIKE_WIDTH = 42;
        int SPIKE_HEIGHT = 64;
        obstacles.add(new SimpleMovingImage(new ImageView("Obstacle3.png"), SPIKE_WIDTH, SPIKE_HEIGHT,objectSpeed,0));

        //change image view
        ImageView imgV = obstacles.getLast().getImage();
        SimpleMovingImage obstacle = obstacles.getLast();

        //set to the right spike image (random spike on the current planet)
        imgV.setViewport(new Rectangle2D((int)(Math.random()*6)*21,currentPlanetInt*32,21,32));

        //add imageView to game objects
        gameObjects.getChildren().add(imgV);

        //set obstacle to the right position
        obstacle.setY(GROUND_Y-obstacle.getHeight());
        obstacle.setX(screenWidth);//CHECK

    }
    public void createMeteorite(){
        //add new meteorite
        int METEO_WIDTH = 38 * 2;
        int METEO_HEIGHT = 32 * 2;
        obstacles.add(new SimpleMovingImage(new ImageView("MeteoriteSheet.png"), METEO_WIDTH, METEO_HEIGHT,objectSpeed,0));

        //change image view
        ImageView imgV = obstacles.getLast().getImage();
        imgV.setViewport(new Rectangle2D(0,0,38,32));

        //add imageView to game objects
        gameObjects.getChildren().add(imgV);

        //set obstacle to the right position
        SimpleMovingImage obstacle = obstacles.getLast();
        obstacle.setY(Math.random()*(GROUND_Y-obstacle.getHeight()));
        obstacle.setX(screenWidth);//CHECK
    }
    public void initializeStar(){
        //changin lin
        star = new Star(new ImageView("StarSheet.png"),52,32, 0, 0, 0);

        //star = new Star(new ImageView("test"),55,48, 0, 0, 0);
        star.setX(screenWidth);
    }
    public void createBackground(){
        background = new Background(new ImageView("Background.png"),2000,500,512,128,objectSpeed ,512,currentPlanetInt);
    }
    public void spawnStar(double x,double y,float speedX, float speedY, double scoreValue){
        star.setX(x);
        star.setY(y);
        star.setSpeedX(speedX);
        star.setSpeedY(speedY);
        star.setScoreValue(scoreValue);
    }

    //PLANET METHOD
    private void planetChange() {
        levelChanger = new Timeline(new KeyFrame(Duration.seconds(7), event -> {
            System.out.println("Change, score:" + score + " speed:" + objectSpeed + "highscoreOLD: " + IOMethods.getHighScore());
            currentPlanetInt = (int) (Math.random() * 7);
            //update background
            background.changePlanet(currentPlanetInt);
            //add planet to planetsDiscovered
            if(planetsDiscovered.charAt(currentPlanetInt) == '0') {
                planetsDiscovered.setCharAt(currentPlanetInt, '1');
            }
            //Change media for music
            mediaPlayer.stop();
            File file = new File(planetArray.get(currentPlanetInt).toString() + "Music.mp3");
            Media media = new Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            //mediaPlayer.play();

            //if player dead stop loop
            if(stopAnimationTimer){
                levelChanger.stop();}
        }));

        // Set the Timeline to run indefinitely
        levelChanger.setCycleCount(Timeline.INDEFINITE);
        levelChanger.play();
    }

    //GAMEPLAY METHODS
    private void gameObjectSpawner(long now){
        // Spawn a new obstacle if the spawn interval has passed
        if (now - lastObstacleSpawnTime >= obstacleSpawnIntervalNano) {
            spawnObstacle();
            lastObstacleSpawnTime = now;
            increaseSpawnSpeed(); // Gradually increase spawn speed
        }
        // Spawn a new star if the spawn interval has passed
        if (now - lastStarSpawnTime >= starSpawnIntervalNano) {
            //give a score value based on time passed
            long scoreValue = (long) 500*1_000_000_000/obstacleSpawnIntervalNano;
            //System.out.println(scoreValue + "scoreValue");
            spawnStar(screenWidth,Math.random()*(GROUND_Y-star.getHeight()),objectSpeed,0,scoreValue);
            lastStarSpawnTime = now;
            randomizeStarSpawnTime(); // Gradually increase spawn speed
        }


    }
    private void spawnObstacle(){
        //REMOVE LATER
        //currentPlanetInt=0;
        int index = (int)(Math.round(Math.random()));
        switch(index){
            case 0: //spawn spike
                createSpike();
                break;
            case 1: //spawn meteor
                createMeteorite();
                break;
        }
    }
    public void randomizeStarSpawnTime(){
        starSpawnIntervalNano = (long)(((Math.random()*35)+10)*1_000_000_000);
    }
    private void increaseSpawnSpeed() {
        // Decrease the spawn interval (make it faster)
        obstacleSpawnIntervalNano = Math.max(200_000_000, obstacleSpawnIntervalNano - spawnIntervalDecrement); // Don't go below 0.2 seconds
    }
    private void updateGameObjectsSpeed(){
        //update background
        background.setSpeedX(objectSpeed);
        //update obstacles
        for(int i =0;i<obstacles.size();i++){
            obstacles.get(i).setSpeedX(objectSpeed);
        }
        //update star
        if(star.getSpeedX()!=0)
            star.setSpeedX(objectSpeed);

    }

    public ImageView isBlackedOut(int planetInt) {
        if(IOMethods.getPlanetsDiscovered().charAt(planetInt) == '0') {
            return new ImageView(planetArray.get(planetInt).name + "BlackedOut.png");
        }
        else {
            return new ImageView(planetArray.get(planetInt).name + ".png");
        }
    }


}