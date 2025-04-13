package com.example.astrojumppseudocode;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static javafx.scene.text.TextAlignment.CENTER;
import static javafx.scene.text.TextAlignment.LEFT;

public class AstroJump extends Application {

    //gameloop method propreties
    private static final int TARGET_FPS = 60;
    private static final long NANOSECONDS_PER_FRAME = 1_000_000_000 / TARGET_FPS;
    private long lastUpdateMethodTime = 0;
    private Timeline levelChanger;

    private float objectSpeed;
    private long score = 0;
    private static long startSpawnPortalStarTime =0;
    private StringBuilder planetsDiscovered = new StringBuilder("00000000");

    private long obstacleSpawnIntervalNano;
    private long starSpawnIntervalNano;
    private long portalSpawnIntervalNano;
    private long lastObstacleSpawnTime =0;
    private long lastStarSpawnTime = 0;
    private long lastPortalSpawnTime =0;

    public static double screenHeight = 500;
    public static double screenWidth = 1000;

    public int currentPlanetInt = currentPlanetInt = (int) Math.round((Math.random() * 7));

    public static boolean stopAnimationTimer;

    public MediaPlayer mediaPlayer;

    //GAME OBJECTS
    private Group gameObjects;
    private Text txGameInfo;
    private Text txGameOver;
    private Text txNetInfo;

    //player
    private Player player;
    private static  int PLAYER_WIDTH;
    private static int PLAYER_HEIGHT;

    //obstacles
    private final ArrayList<SimpleMovingImage> obstacles = new ArrayList<>();
    private static int SPIKE_WIDTH;
    private static int SPIKE_HEIGHT;
    private static int METEO_WIDTH;
    private static int METEO_HEIGHT;

    //star
    private Star star;
    private static int STAR_WIDTH;
    private static int STAR_HEIGHT;

    //net
    private final ArrayList<Net> nets = new ArrayList<>();
    private static int NET_SIZE;
    private Path parabolaPath;
    private boolean updatePath = false;
    private double lastKnownMouseX;
    private double lastKnownMouseY;


    //Background
    Background background;
    private static int BACKGROUND_WIDTH;
    private static int BACKGROUND_HEIGHT;

    //Portal
    SimpleMovingImage portal;
    private static int PORTAL_WIDTH;
    private static int PORTAL_HEIGHT;

    //planets
    protected static ArrayList<Planet> planetArray;

    //game pane properties
    private static int GROUND_Y;
    private static double definingSize;

    //game controls
    private static String jumpControl = "SPACE";
    private String netThrow = "";

    public static void main(String[] args) {
        //get screen resolution
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenWidth = screenSize.getWidth();
        screenHeight = screenSize.getHeight();
        //defining "used" width/height
        if(!(screenWidth/screenHeight >= 2)) {
            definingSize = screenHeight/500.0;

        }
        else {
            definingSize = (screenWidth/1000.0);

        }
        //adjusting game objects based on screen resolution

        PLAYER_WIDTH = (int) (100 * definingSize);
        SPIKE_WIDTH = (int) (42 * definingSize);
        SPIKE_HEIGHT = (int) (64 * definingSize);
        METEO_WIDTH = (int) (105 *  definingSize);
        METEO_HEIGHT = (int) (30 *  definingSize);
        STAR_WIDTH = (int) (58 *  definingSize);
        STAR_HEIGHT = (int) (60 *  definingSize);
        NET_SIZE = (int) (45 *  definingSize);
        BACKGROUND_WIDTH = (int) (2000 * definingSize);
        BACKGROUND_HEIGHT = (int) (500 *  definingSize);
        PORTAL_WIDTH = (int) (96 *  definingSize);
        PORTAL_HEIGHT = (int) (96 * definingSize);
        GROUND_Y = (int) (390 * definingSize);
        PLAYER_HEIGHT = (int) (100 * definingSize);

        //initiate planetArray with gravities from NSSDC
        Planet mercury = new Planet("Mercury", (float) (-567f*definingSize), (float) (-600f*  definingSize), (float) (600f*  definingSize),0,0,30);
        Planet venus = new Planet("Venus", (float) (-1382f*  definingSize), (float) (-900f*  definingSize), (float) (600f*  definingSize),-6,0,30);
        Planet earth = new Planet("Earth", (float) (-1524f*  definingSize), (float) (-1000f*  definingSize), (float) (600f*  definingSize),-8,0,30);
        Planet mars = new Planet("Mars", (float) (-574f*  definingSize), (float) (-600f*  definingSize), (float) (600f*  definingSize),0,0,30);
        Planet jupiter = new Planet("Jupiter", (float) (-3596f*  definingSize), (float) (-1500f*  definingSize), (float) (600f*  definingSize),0,0,30);
        Planet saturn = new Planet("Saturn", (float) (-1396f*  definingSize), (float) (-900f*  definingSize), (float) (600f*  definingSize),-5,-16.5,64);
        Planet uranus = new Planet("Uranus", (float) (-1355f*  definingSize), (float) (-1000f*  definingSize), (float) (600f*  definingSize),-7.5,-2.5,36);
        Planet neptune = new Planet("Neptune", (float) (-1707f*  definingSize), (float) (-1000f*  definingSize), (float) (600f*  definingSize),0,0,30);
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
        GridPane generalPane = new GridPane();
        Text txFiller = new Text("\n\n\n");

        // High score and total stars collected
        VBox vBox1 = new VBox();
        vBox1.setAlignment(Pos.BOTTOM_CENTER);
        vBox1.setSpacing(20);
        vBox1.setPadding(new Insets(0, 0, 0, 150));
        Label lbScore = new Label("High Score:");
        lbScore.setStyle("-fx-font-size: 30px;");
        TextField tfScore = new TextField(IOMethods.getHighScore() + "");
        tfScore.setStyle("-fx-background-color: lavender;\n-fx-stroke-line-join: miter;\n-fx-border-color: black;\n-fx-border-width: 1.8;\n-fx-font-size: 20px;");
        tfScore.setAlignment(Pos.CENTER);
        tfScore.setEditable(false);
        Label lbStars = new Label("Total Stars Collected:");
        lbStars.setStyle("-fx-font-size: 30px;");
        lbStars.setMinWidth(200*definingSize);
        TextField tfStars = new TextField(IOMethods.getTotalStarsCollected() + "");
        tfStars.setStyle("-fx-background-color: lavender;\n-fx-stroke-line-join: miter;\n-fx-border-color: black;\n-fx-border-width: 1.8;\n-fx-font-size: 20px;");
        tfStars.setAlignment(Pos.CENTER);
        tfStars.setEditable(false);
        vBox1.getChildren().addAll(lbScore, tfScore, lbStars, tfStars);
        generalPane.add(vBox1,0,1);

        // Start, tutorial, settings and exit
        VBox vBox2 = new VBox();
        vBox2.setAlignment(Pos.BOTTOM_CENTER);
        vBox2.setSpacing(20);

        // Start button
        Button btStart = new Button("Start");
        btStart.setPrefWidth(250);
        btStart.setPrefHeight(30);
        btStart.setStyle("-fx-background-color: lavender;\n-fx-stroke-line-join: miter;\n-fx-border-color: black;\n-fx-border-width: 1.8;\n-fx-font-size: 20px;");


        // Tutorial button
        Button btTutorial = new Button("Tutorial");
        btTutorial.setPrefWidth(250);
        btTutorial.setPrefHeight(30);
        btTutorial.setStyle("-fx-background-color: lavender;\n-fx-stroke-line-join: miter;\n-fx-border-color: black;\n-fx-border-width: 1.8;\n-fx-font-size: 20px;");

        // Settings button
        Button btSettings = new Button("Settings");
        btSettings.setPrefWidth(250);
        btSettings.setPrefHeight(30);
        btSettings.setStyle("-fx-background-color: lavender;\n-fx-stroke-line-join: miter;\n-fx-border-color: black;\n-fx-border-width: 1.8;\n-fx-font-size: 20px;");

        // Exit button
        Button btExit = new Button("Exit");
        btExit.setPrefWidth(250);
        btExit.setPrefHeight(30);
        btExit.setStyle("-fx-background-color: lavender;\n-fx-stroke-line-join: miter;\n-fx-border-color: black;\n-fx-border-width: 1.8;\n-fx-font-size: 20px;");

        vBox2.getChildren().addAll(txFiller, btStart, btTutorial, btSettings, btExit);
        generalPane.add(vBox2, 1,1);

        // Planets discovered
        VBox vBox3 = new VBox();
        vBox3.setAlignment(Pos.BOTTOM_CENTER);
        vBox3.setSpacing(20);
        vBox3.setPadding(new Insets(0, 150, 0, 0));
        Label lbPlanets = new Label("Planets Discovered:");
        lbPlanets.setStyle("-fx-font-size: 30px;");
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(5);

        //Setting planet image
        for(int i = 0; i < 8; i++) {
            double ratio = definingSize;
            VBox vbPlanet = new VBox();
            vbPlanet.setAlignment(Pos.CENTER);
            vbPlanet.setSpacing(5*ratio);
            ImageView planetImage = new ImageView("file:" + planetArray.get(i).toString() + ".png");
            planetImage.setFitWidth(planetArray.get(i).getSize()*ratio);
            planetImage.setFitHeight(planetArray.get(i).getSize()*ratio);
            ColorAdjust blackout = new ColorAdjust();
            blackout.setBrightness(-1);
            planetImage.setTranslateX(planetArray.get(i).getSetTranslateX()*ratio);

            if(isBlackedOut(i)){
                planetImage.setEffect(blackout);
                Label lbPlanet = new Label("???");
                lbPlanet.setTranslateX(planetArray.get(i).getSetTranslateX()*ratio);
                lbPlanet.setTranslateY(planetArray.get(i).getSetTranslateY()*ratio);
                vbPlanet.getChildren().addAll(planetImage, lbPlanet);
            }
            else {
                Tooltip tooltipPlanet = new Tooltip("Gravitational acceleration\nof " + planetArray.get(i).toString() + " : " + Math.round(planetArray.get(i).gravity/-1.5551)/100.0 + " m/s²");
                tooltipPlanet.setTextAlignment(CENTER);
                Tooltip.install(planetImage, tooltipPlanet);
                Label lbPlanet = new Label(planetArray.get(i).toString());
                lbPlanet.setTranslateX(planetArray.get(i).getSetTranslateX()*ratio);
                lbPlanet.setTranslateY(planetArray.get(i).getSetTranslateY()*ratio);
                vbPlanet.getChildren().addAll(planetImage, lbPlanet);
            }
            //add to gridpane
            gridPane.add(vbPlanet, i%4, i/4);
        }
        vBox3.getChildren().addAll(txFiller, lbPlanets, gridPane);
        generalPane.add(vBox3,2,1);

        // Setting page
        BorderPane borderPane1 = new BorderPane();

        // Action
        VBox vbox1 = new VBox();
        vbox1.setAlignment(Pos.CENTER);
        vbox1.setSpacing(40);
        vbox1.setPadding(new Insets(0, 150, 30, 150));
        Label lbAction = new Label("ACTION:");
        lbAction.setFont(new Font(40));
        Label lbJump = new Label("Jump:");
        lbJump.setFont(new Font(30));
        Label lbThrow = new Label("Throw:");
        lbThrow.setFont(new Font(30));
        Label lbExit = new Label("Exit:");
        lbExit.setFont(new Font(30));
        Label lbAim = new Label("Aim:");
        lbAim.setFont(new Font(30));
        vbox1.getChildren().addAll(lbAction, lbJump, lbThrow, lbExit, lbAim);
        borderPane1.setLeft(vbox1);

        // Control
        VBox vbox2 = new VBox();
        vbox2.setAlignment(Pos.CENTER);
        vbox2.setSpacing(43);
        vbox2.setPadding(new Insets(0, 0, 20, 0));
        Label lbControls = new Label("CONTROL:");
        lbControls.setFont(new Font(40));
        lbControls.setTranslateY(-2.5);

        TextField tfJump = new TextField("SPACEBAR");
        tfJump.setFont(new Font(20));
        tfJump.setAlignment(Pos.CENTER);
        tfJump.setEditable(false);

        TextField tfThrow = new TextField("RIGHT CLICK");
        tfThrow.setFont(new Font(20));
        tfThrow.setAlignment(Pos.CENTER);
        tfThrow.setTranslateY(-3);
        tfThrow.setEditable(false);

        TextField tfExit = new TextField("ESCAPE");
        tfExit.setFont(new Font(20));
        tfExit.setAlignment(Pos.CENTER);
        tfExit.setTranslateY(-5);
        tfExit.setEditable(false);

        TextField tfAim = new TextField("MOUSE");
        tfAim.setFont(new Font(20));
        tfAim.setAlignment(Pos.CENTER);
        tfAim.setTranslateY(-7);
        tfAim.setEditable(false);

        vbox2.getChildren().addAll(lbControls, tfJump, tfThrow, tfExit, tfAim);
        borderPane1.setCenter(vbox2);

        // Sound setting
        VBox vbox3 = new VBox();
        vbox3.setAlignment(Pos.CENTER);
        vbox3.setSpacing(30);
        vbox3.setPadding(new Insets(0, 150, 8, 150));
        Label lbSound = new Label("SOUND SETTING:");
        lbSound.setFont(new Font(40));
        lbSound.setTranslateY(15);

        // Music
        Label lbMusic = new Label("Music:");
        lbMusic.setFont(new Font(30));
        lbMusic.setPadding(new Insets(25,0,0,0));

        // Sound Effects
        Label lbEffects = new Label("Sound Effects:");
        lbEffects.setFont(new Font(30));
        vbox3.getChildren().addAll(lbSound, lbMusic, createSlider(), lbEffects, createSlider());
        borderPane1.setRight(vbox3);


        //buttons action handler
        btStart.setOnAction(e -> startGameLoop(primaryStage));
        btTutorial.setOnAction(e -> showTutorial(primaryStage));
        btSettings.setOnAction(e -> showSettings(primaryStage));
        btExit.setOnAction(e -> System.exit(0));


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
        generalPane.setHgap(125);
        generalPane.setVgap(100);
        generalPane.setAlignment(Pos.CENTER);
        Image menuImage = new Image("file:menuScreen.bmp");
        generalPane.setBackground(new javafx.scene.layout.Background(new BackgroundImage(menuImage,BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,new BackgroundSize(screenWidth,screenHeight,true,true,true,true))));
        StackPane mediaAndVisuals = new StackPane(pane,generalPane);
        Scene scene = new Scene(mediaAndVisuals,screenWidth,screenHeight);
        mediaAndVisuals.requestFocus();

        // Go back button
        BorderPane borderPane2 = new BorderPane();
        borderPane2.setPadding(new Insets(-50, 50, 50, 50));
        Button btBack = new Button("Back");
        btBack.setFont(new Font(20));
        btBack.setOnAction(e -> primaryStage.setScene(scene));
        borderPane2.setLeft(btBack);
        borderPane1.setBottom(borderPane2);

        Scene scene2 = new Scene(borderPane1, screenWidth, screenHeight);
        btSettings.setOnAction(e -> primaryStage.setScene(scene2));
        primaryStage.setTitle("AstroJump");

        //listener for back button
        scene2.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                primaryStage.setScene(scene);
            }
        });

        //create player
        createPlayer();

        //initialize star
        initializeStar();

        //create background
        createBackground();


        primaryStage.setScene(scene);
        primaryStage.show();


    }

    //show different stages
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

    //GAMELOOP METHODS
    protected void startGameLoop(Stage primaryStage) {
        score = 0;
        player.setStarsCaught(0);
        //add first planet to planetsDiscovered
        if(!IOMethods.getPlanetsDiscovered().isEmpty()) {
            planetsDiscovered = IOMethods.getPlanetsDiscovered();
        }
        planetsDiscovered = IOMethods.getPlanetsDiscovered();
        //add planet to planetsDiscovered
        if(planetsDiscovered.charAt(currentPlanetInt) == '0') {
            planetsDiscovered.setCharAt(currentPlanetInt, '1');
        }

        //start all timers
        stopAnimationTimer = false;

        //game scene setup
        objectSpeed = -500;
        if(gameObjects!=null)
            gameObjects.getChildren().clear();
        score = 0;

        //initialise txGameInfo
        txGameInfo = new Text( "Current Planet: " + planetArray.get(currentPlanetInt).toString() + "\nCurrent Gravity: " + Math.round(planetArray.get(currentPlanetInt).gravity/-1.5551)/100.0/definingSize + " m/s*s\nScore: " + score + "\nStars: " + player.getStarsCaught());
        txGameInfo.setFont(Font.font("Copperplate Gothic Bold", FontWeight.NORMAL, FontPosture.REGULAR,15 * definingSize));
        txGameInfo.setFill(Color.CORNFLOWERBLUE);
        txGameInfo.setStrokeWidth(.8 * definingSize);
        txGameInfo.setStroke(Color.BLACK);
        txGameInfo.setTextAlignment(LEFT);
        txGameInfo.setX(6 * definingSize);
        txGameInfo.setY(22 * definingSize);

        //initialise txGameOver
        txGameOver = new Text("");
        txGameOver.setFont(Font.font("Copperplate Gothic Bold", FontWeight.NORMAL, FontPosture.REGULAR, 20 * definingSize));
        txGameOver.setFill(Color.CORNFLOWERBLUE);
        txGameOver.setStrokeWidth(0.8 * definingSize);
        txGameOver.setStroke(Color.BLACK);
        txGameOver.setTextAlignment(CENTER);
        BorderPane pane = new BorderPane();
        pane.setPrefSize(screenWidth,screenHeight);
        pane.setCenter(txGameOver);

        //initialise net info text
        txNetInfo = new Text("");
        txNetInfo.setFont(Font.font("Copperplate Gothic Bold", FontWeight.NORMAL, FontPosture.REGULAR, 15*definingSize));
        txNetInfo.setFill(Color.WHITE);
        txNetInfo.setStrokeWidth(.8*definingSize);
        txNetInfo.setStroke(Color.BLACK);
        txNetInfo.setTextAlignment(LEFT);
        txNetInfo.setX(screenWidth-265*definingSize);
        txNetInfo.setY(20*definingSize);
        txNetInfo.setTextAlignment(TextAlignment.RIGHT);


        gameObjects = new Group(background.getImage(),player.getImage(),star.getImage(), txGameInfo,txNetInfo, pane);


        //clear nets, obstacles
        nets.clear();
        obstacles.clear();

        //reset timers
        obstacleSpawnIntervalNano = (long)3.5*1_000_000_000;
        starSpawnIntervalNano= (long)4*1_000_000_000;
        portalSpawnIntervalNano = (long)30*1_000_000_000;

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
        game.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.R) {
                try {
                    IOMethods saveData = new IOMethods(score, player.getStarsCaught(), planetsDiscovered);
                    score = 0;
                    player.setStarsCaught(0);
                    startGameLoop(primaryStage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        //jump event handler
        game.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE&&!player.getIsJumping()) {
                player.setIsJumping(true);
                player.setY(GROUND_Y-1-player.getHeight());
            }
        });

        // when mouse is released create a net
        game.setOnMouseReleased(event -> {
            parabolaPath.getElements().clear();
            txNetInfo.setText("");
            updatePath = false;
            createNet(planetArray.get(currentPlanetInt).getGravity(),planetArray.get(currentPlanetInt).getNetForce());
        });
        game.setOnMousePressed(event -> {
            updatePath = true;
            //update mouse positions
            lastKnownMouseX = event.getX();
            lastKnownMouseY = event.getY();
        });
        game.setOnMouseDragged(event -> {
            //update mouse
            lastKnownMouseX = event.getX();
            lastKnownMouseY = event.getY();
        });

        primaryStage.setScene(game);
        primaryStage.show();

        //create and animationTimer to call to update method and gameObjectSpawner
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Calculate the time elapsed since the last frame
                if(stopAnimationTimer) {
                    this.stop();
                }
                if (lastUpdateMethodTime > 0) {
                    long elapsedTime = now - lastUpdateMethodTime;
                    // If enough time has passed, call update and save lastUpdateTime
                    if (elapsedTime >= NANOSECONDS_PER_FRAME) {
                        //update score
                        score += (long) ((-0.0000000001) * elapsedTime * objectSpeed);
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

        //update txGameInfo
        txGameInfo.setText("Current Planet: " + planetArray.get(currentPlanetInt).toString() + "\nCurrent Gravitiy: " + Math.round(planetArray.get(currentPlanetInt).gravity/-1.5551/definingSize)/100.0 + " m/s²\nScore: " + score + "\nStars: " + player.getStarsCaught());

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
                    score+= (long) star.getScoreValue();
                    //reset star
                    spawnStar(screenWidth,0,0,0,0);
                }
            }
            //check for collision with ground
            if(net.getY()+net.getHeight()>=GROUND_Y){
                //delete game object
                gameObjects.getChildren().remove(net.getImage());
                nets.remove(net);
                net=null;
                i--;
            }
        }

        //OBSTACLE updates
        for(int i =0;i<obstacles.size();i++){
            SimpleMovingImage obstacle = obstacles.get(i);

            //update position
            obstacle.updatePosition(deltaTime);

            //check for collisions with player
            if(obstacle.isCollidingWith(player.getImage())){
                //game over methods
                IOMethods saveData = new IOMethods(score, player.getStarsCaught(), planetsDiscovered);
                stopAnimationTimer = true;
                player.setAnimationState(Player.DEAD);
                txGameOver.setText("GAME OVER\nPRESS ESCAPE TO EXIT TO MAIN MENU OR R TO RESTART");
                //showGameOverScreen(stage);
            }

            //if the obstacle is out of bounds delete it
            if(obstacle.isOutOfBounds(GROUND_Y)){
                gameObjects.getChildren().remove(obstacle.getImage());
                obstacle=null;
                obstacles.remove(i);
                i--;
            }

            //check for collision with portal
            if(portal !=null & obstacle!=null) {
                if (obstacle.isCollidingWith(portal.getImage())){
                    //delete obstacle
                    gameObjects.getChildren().remove(obstacle.getImage());
                    obstacle=null;
                    obstacles.remove(i);
                    i--;
                }
            }
        }

        //STAR movement and collisions
        star.updatePosition(deltaTime);

        if(star.isCollidingWith(player.getImage())){
            player.addOneStar();
            score+=(long)star.getScoreValue();
            //reset star
            spawnStar(screenWidth,0,0,0,0);
        }
        //reset the star if it is out of bounds
        if(star.isOutOfBounds(GROUND_Y)){
            spawnStar(screenWidth,0,0,0,0);
        }

        //background update
        background.updatePosition(deltaTime);

        //portal update and position
        if(portal!=null) {
            //update pos
            portal.updatePosition(deltaTime);
            //out of bounds deletion
            if (portal.isOutOfBounds(GROUND_Y)) {
                //delete portal
                gameObjects.getChildren().remove(portal.getImage());
                portal = null;
            }
        }
        //collision detection for portal
        if(portal!=null) {
            if (portal.isCollidingWith(player.getImage())&&!player.getIsJumping()) {
                //change planet
                changePlanet();
                //update spikes graphics
                updateSpikePlanet();
                //delete portal
                gameObjects.getChildren().remove(portal.getImage());
                portal = null;
            }
        }

        //Path updated
        if(updatePath){
            //calculate the inital position of the net
            double initialPosX = player.getX()+player.getWidth();
            double initialPosY = player.getY()+(0.5*player.getHeight())-(Net.WIDTH*0.5);

            //calculate the angle of the throw
            double dx = lastKnownMouseX - initialPosX;
            double dy = -lastKnownMouseY + initialPosY;

            // Angle from player to point (world angle)
            double angle = Math.atan2(dy, dx);

            //calculate the speed in each axis
            double initialSpeedX = planetArray.get(currentPlanetInt).getNetForce()* Math.cos(angle);
            double initialSpeedY = -planetArray.get(currentPlanetInt).getNetForce()* Math.sin(angle);
            //get acceleration
            double accelerationY = planetArray.get(currentPlanetInt).getGravity();
            double accelerationX = 0;

            //draw line
            // Create a path for the parabola
            double k = (Math.pow(initialSpeedY,2)/(2*accelerationY))+initialPosY;
            double h = initialSpeedX*initialSpeedY/accelerationY+initialPosX;

            //calculate a
            double a = (initialPosY-k)/Math.pow((initialPosX-h),2);

            //parabolaPath = createParabolaApproximation(net.getAccelerationY(),h,k);
            parabolaPath = createParabola(parabolaPath,a,h,k,initialPosX);

            //add parabolaPath if it isn't already there
            if(!gameObjects.getChildren().contains(parabolaPath))
                gameObjects.getChildren().add(parabolaPath);

            //UPDATE NET INFO
            txNetInfo.setText("NET THROW INFO:\n" +
                    "Initial Speed: " + planetArray.get(currentPlanetInt).getNetForce()+ "(px/s)" +
                    "\nAngle thrown: " + roundTo2Dec(Math.toDegrees(angle)) + "(degrees)" +
                    "\nInitial Speed X: " + (int)initialSpeedX + "(px/s)" +
                    "\nInitial Speed Y: " + (int)-initialSpeedY + "(px/s)" +
                    "\nAcceleration X: " + accelerationX + "(px/s²)" +
                    "\nAcceleration Y: "+ roundTo2Dec(accelerationY)+ "(px/s²)");
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
        ImageView playerIV = new ImageView(IMAGE);
        //set player imageView to first image
        playerIV.setViewport(new Rectangle2D(OFFSET_X, OFFSET_Y, SPRITE_WIDTH, SPRITE_HEIGHT));
        playerIV.setFitWidth(PLAYER_WIDTH);
        playerIV.setFitHeight(PLAYER_HEIGHT);

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
    public void createNet(float gravity, float netForce){
        //calculate the inital position of the net
        double initialPosX = player.getX()+player.getWidth();
        double initialPosY = player.getY()+(0.5*player.getHeight())-(0.5* NET_SIZE);

        //calculate the angle of the throw
        double dx = lastKnownMouseX - initialPosX;
        double dy = -lastKnownMouseY + initialPosY;

        // Angle from player to point (world angle)
        double angle = Math.atan2(dy, dx);

        //calculate the speed in each axis
        double initialSpeedX = netForce* Math.cos(angle);
        double initialSpeedY = -netForce* Math.sin(angle);

        //if the player is aiming backwards shot the net like a slingshot
        if(initialSpeedX<0){
            initialSpeedX*=-1;
            initialSpeedY*=-1;
        }
        //create net

        //adds a new net to the nets array
        nets.add(new Net(new ImageView("Net.png"),initialPosX,initialPosY,initialSpeedX,initialSpeedY,gravity,0));
        //adds the last net added to nets to the game object group
        gameObjects.getChildren().add(nets.getLast().getImage());
        //set its initial Positions to the nets initial positions
        nets.getLast().setX(initialPosX);
        nets.getLast().setY(initialPosY);
        nets.getLast().setHeight(NET_SIZE);
        nets.getLast().setWidth(NET_SIZE);


    }

    //OBSTACLE METHODS
    public void createSpike(){
        //add new obstacle
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
        obstacles.add(new SimpleMovingImage(new ImageView("MeteoriteSheet2.png"), METEO_WIDTH, METEO_HEIGHT,objectSpeed,0));

        //set obstacle to the right position
        SimpleMovingImage obstacle = obstacles.getLast();

        //change image view
        ImageView imgV = obstacle.getImage();
        imgV.setViewport(new Rectangle2D(0,(Math.round(Math.random()))*10,35,10));

        obstacle.setY(Math.random()*(GROUND_Y-obstacle.getHeight()-player.getHeight()));
        obstacle.setX(screenWidth);//CHECK
        //add imageView to game objects
        gameObjects.getChildren().add(obstacles.getLast().getImage());
    }
    public void updateSpikePlanet(){
        for(int i =0; i<obstacles.size();i++){
            SimpleMovingImage obstacle = obstacles.get(i);
            //make sure the object is a spike
            if(obstacle.getHeight()==SPIKE_HEIGHT&&obstacle.getWidth()==SPIKE_WIDTH){
                //change the viewport to the right planet
                //set to the right spike image (same spike on the current planet)
                 Rectangle2D oldViewport = obstacle.getImage().getViewport();
                obstacle.getImage().setViewport(new Rectangle2D(oldViewport.getMinX(), currentPlanetInt*32,21,32));
            }
        }
    }

    //STAR METHODS
    public void initializeStar(){
        //create star
        star = new Star(new ImageView("StarAnimationSheet.png"),STAR_WIDTH, STAR_HEIGHT, 0, 0, 0);

        //get random index 0-1
        int index = (int)(Math.round(Math.random()));

        //randomize the star
        star.getImage().setViewport(new Rectangle2D(index*29,0,29,30));

        //set position and size
        star.setX(screenWidth);
    }
    public void spawnStar(double x,double y,float speedX, float speedY, double scoreValue){
        star.setX(x);
        star.setY(y);
        star.setSpeedX(speedX);
        star.setSpeedY(speedY);
        star.setScoreValue(scoreValue);

        //get random index 0-1
        int index = (int)(Math.round(Math.random()));
        //randomize the star image
        star.getImage().setViewport(new Rectangle2D(index*29,0,29,30));
    }

    //BACKGROUND METHOD
    public void createBackground(){
        background = new Background(new ImageView("Background.png"),BACKGROUND_WIDTH,BACKGROUND_HEIGHT,512,128,objectSpeed ,512,currentPlanetInt);
    }

    //PATH METHOD
    private Path createParabola(Path path,double a, double h, double k,double startX) {
        //reset the path is if it doesn't exist already
        if(path==null)
            path = new Path();
        else
            path.getElements().clear();

        // Style the path as a white dotted line
        path.setStroke(Color.WHITE);
        path.setStrokeWidth(2);
        path.getStrokeDashArray().addAll(5.0, 5.0); // This creates the dotted effect
        path.setFill(null); // Important for a line (not filled)

        double endX = screenWidth;    // End at screen width
        double step = 1;          // Smaller step = smoother curve

        // Move to starting point
        double startY = a * Math.pow(startX - h, 2) + k+ Net.WIDTH*0.5;
        path.getElements().add(new MoveTo(startX, startY));

        // Create the parabola by adding line segments
        for (double x = startX + step; x <= endX; x += step) {
            double y = a * Math.pow(x - h, 2) + k + Net.WIDTH*0.5;
            path.getElements().add(new LineTo(x, y));
        }

        return path;
    }

    //PORTAL METHOD
    private void spawnPortal() {
            //spawn portal
            portal = new SimpleMovingImage(new ImageView("Black_hole.png"),PORTAL_WIDTH,PORTAL_HEIGHT,objectSpeed,0);
            portal.setX(screenWidth);
            portal.setY(GROUND_Y-portal.getHeight());
            gameObjects.getChildren().add(portal.getImage());
    }

    //PLANET CHANGE METHOD
    private void changePlanet(){
        System.out.println("Change, score:" + score + " speed:" + objectSpeed + "highscoreOLD: " + IOMethods.getHighScore());
        int newPlanetInt = (int) Math.round((Math.random() * 7));
        while(currentPlanetInt == newPlanetInt) {
            newPlanetInt = (int) Math.round((Math.random() * 7));
        }
        currentPlanetInt = newPlanetInt;
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
            levelChanger.stop();
        }
    }

    //GAMEPLAY SPAWNING METHODS
    private void gameObjectSpawner(long now){

        //initialize spawning time for star and portal
        if(startSpawnPortalStarTime ==0) {
            startSpawnPortalStarTime = now + 1_000_000_000;
            lastPortalSpawnTime = now;
            lastStarSpawnTime = now;
        }

        // Spawn a new obstacle if the spawn interval has passed
        if (now - lastObstacleSpawnTime >= obstacleSpawnIntervalNano) {
            spawnObstacle();
            lastObstacleSpawnTime = now;
            increaseSpawnSpeed(); // Gradually increase spawn speed
        }
        // Spawn a new star if the spawn interval has passed
        if (now - lastStarSpawnTime >= starSpawnIntervalNano&&now> startSpawnPortalStarTime) {
            //give a score value based on time passed
            long scoreValue = (long) 500*1_000_000_000/obstacleSpawnIntervalNano;

            spawnStar(Math.random()*(screenWidth-star.getWidth()),Math.random()*(GROUND_Y-star.getHeight()),0,0,scoreValue);

            lastStarSpawnTime = now;
            randomizeStarSpawnTime(); // Gradually increase spawn speed
        }

        //System.out.println(now);

        // Spawn a new portal if the spawn interval has passed
        if (now - lastPortalSpawnTime >= portalSpawnIntervalNano && now> startSpawnPortalStarTime) {
            spawnPortal();

            lastPortalSpawnTime = now;
            randomizePortalSpawnTime(); // Gradually increase spawn speed
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
        starSpawnIntervalNano = (long)(((Math.random()*5)+10)*1_000_000_000);
    }
    public void randomizePortalSpawnTime(){
        portalSpawnIntervalNano = (long)(((Math.random()*10)+20)*1_000_000_000);
    }
    private void increaseSpawnSpeed() {
        // Decrease the spawn interval (make it faster)
        long SPAWN_TIME_DECREMENT = 20_000_000;
        obstacleSpawnIntervalNano = Math.max(300_000_000, obstacleSpawnIntervalNano - SPAWN_TIME_DECREMENT);// Don't go below 0.2 seconds
        System.out.println(obstacleSpawnIntervalNano);
    }
    private void updateGameObjectsSpeed(){
        //update background
        background.setSpeedX(objectSpeed);
        //update obstacles
        for (SimpleMovingImage obstacle : obstacles) {
            obstacle.setSpeedX(objectSpeed);
        }
        //update star
        // if(star.getSpeedX()!=0)
        //star.setSpeedX(objectSpeed);

    }

    //MENU METHOD
    public boolean isBlackedOut(int planetInt) {
        if(IOMethods.getPlanetsDiscovered().charAt(planetInt) == '0') {
            return true;
        }
        else {
            return false;
        }
    }

    public Node createSlider() {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        Slider slider = new Slider();
        slider.setMin(0);
        slider.setMax(100);
        Label lbSlider = new Label((int) slider.getValue() + "%");
        lbSlider.setFont(new Font(30));
        lbSlider.setMinWidth(80);
        lbSlider.setMinHeight(80);
        lbSlider.setTranslateX(10);
        lbSlider.setTranslateY(0);
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            lbSlider.setText(newValue.intValue() + "%");
        });
        hbox.getChildren().addAll(slider, lbSlider);
        return hbox;
    }

    //MATH METHOD
    private double roundTo2Dec(double value){
        return ((int)(value*100))/100.0;
    }
}
