package com.example.astrojumppseudocode;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
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
import javafx.scene.control.MenuItem;
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
import javafx.stage.Popup;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static javafx.scene.text.TextAlignment.CENTER;
import static javafx.scene.text.TextAlignment.LEFT;

public class AstroJump extends Application {

    //GLOBAL ATTRIBUTES

    //Game loop objects
    private static final int TARGET_FPS = 60;
    private static final long NANOSECONDS_PER_FRAME = 1_000_000_000 / TARGET_FPS;
    private long lastUpdateMethodTime = 0;

    //User attributes
    private long score = 0;
    private StringBuilder planetsDiscovered = new StringBuilder("00000000");
    public static double screenHeight = 500;
    public static double screenWidth = 1000;
    private static double definingSize;
    private static int musicSliderValue;
    public int currentPlanetInt = (int) Math.round((Math.random() * 7));
    public int currentTutorialInt = 0;
    public static MediaPlayer mediaPlayer;

    //Timer attributes
    private static long startSpawnPortalStarTime = 0;
    private float objectSpeed;
    private long obstacleSpawnIntervalNano;
    private long starSpawnIntervalNano;
    private long portalSpawnIntervalNano;
    private long lastObstacleSpawnTime = 0;
    private long lastStarSpawnTime = 0;
    private long lastPortalSpawnTime = 0;
    public static boolean stopAnimationTimer;

    //General game objects
    private Group gameObjects;
    private Text txGameInfo;
    private Text txGameOver;
    private Text txNetInfo;
    private static int GROUND_Y;

    //Player objects
    private Player player;
    private static int PLAYER_WIDTH;
    private static int PLAYER_HEIGHT;
    private static float PLAYER_JUMP_FORCE;

    //Obstacles attributes
    private final ArrayList<SimpleMovingImage> obstacles = new ArrayList<>();
    private static int SPIKE_WIDTH;
    private static int SPIKE_HEIGHT;
    private static int METEOR_WIDTH;
    private static int METEOR_HEIGHT;

    //Star attributes
    private Star star;
    private static int STAR_WIDTH;
    private static int STAR_HEIGHT;

    //Net attributes
    private final ArrayList<Net> nets = new ArrayList<>();
    private static int NET_SIZE;
    private Path parabolaPath;
    private boolean updatePath = false;
    private double lastKnownMouseX;
    private double lastKnownMouseY;
    private static float NET_FORCE;
    private float windDeceleration;

    //Background attributes
    Background background;
    private static int BACKGROUND_WIDTH;
    private static int BACKGROUND_HEIGHT;

    //Portal attributes
    SimpleMovingImage portal;
    private static int PORTAL_WIDTH;
    private static int PORTAL_HEIGHT;

    //Planets objects
    protected static ArrayList<Planet> planetArray;


    //MAIN METHOD - SETTING USER ATTRIBUTES, PLANET ARRAY, OBJECT SIZE
    public static void main(String[] args) {
        //Get user's screen resolution
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenWidth = screenSize.getWidth();
        screenHeight = screenSize.getHeight();

        //Defining "used" width/height
        if (!(screenWidth / screenHeight >= 2)) {
            definingSize = screenHeight / 500.0;

        } else {
            definingSize = (screenWidth / 1000.0);

        }
        musicSliderValue = 50;

        //Adjusting game objects based on screen resolution
        PLAYER_WIDTH = (int) (50 * definingSize);
        PLAYER_HEIGHT = (int) (50 * definingSize);
        SPIKE_WIDTH = (int) (21 * definingSize);
        SPIKE_HEIGHT = (int) (37 * definingSize);
        METEOR_WIDTH = (int) (105 * definingSize);
        METEOR_HEIGHT = (int) (30 * definingSize);
        STAR_WIDTH = (int) (50 * definingSize);
        STAR_HEIGHT = (int) (50 * definingSize);
        NET_SIZE = (int) (30 * definingSize);
        BACKGROUND_WIDTH = (int) (2000 * definingSize);
        BACKGROUND_HEIGHT = (int) (500 * definingSize);
        PORTAL_WIDTH = (int) (96 * definingSize);
        PORTAL_HEIGHT = (int) (96 * definingSize);
        GROUND_Y = (int) (390 * definingSize);

        //Initiate forces
        PLAYER_JUMP_FORCE = (float) (-800f * definingSize);
        NET_FORCE = (float) (800f * definingSize);

        //Initiate planetArray with gravities from NSSDC
        Planet mercury = new Planet("Mercury", (float) (-576f * definingSize), 0, 0, 30);
        Planet venus = new Planet("Venus", (float) (-1382f * definingSize), -6, 0, 30);
        Planet earth = new Planet("Earth", (float) (-1524f * definingSize), -8, 0, 30);
        Planet mars = new Planet("Mars", (float) (-574f * definingSize), 0, 0, 30);
        Planet jupiter = new Planet("Jupiter", (float) (-3596f * definingSize), 0, 0, 30);
        Planet saturn = new Planet("Saturn", (float) (-1396f * definingSize), -5, -16.5, 64);
        Planet uranus = new Planet("Uranus", (float) (-1355f * definingSize), -7.5, -2.5, 36);
        Planet neptune = new Planet("Neptune", (float) (-1707f * definingSize), 0, 0, 30);
        planetArray = new ArrayList<>();
        planetArray.add(mercury);
        planetArray.add(venus);
        planetArray.add(earth);
        planetArray.add(mars);
        planetArray.add(jupiter);
        planetArray.add(saturn);
        planetArray.add(uranus);
        planetArray.add(neptune);

        //Launch args
        launch(args);
    }


    //SHOW MAIN MENU GUI, MUSIC,
    public void start(Stage primaryStage) throws IOException {
        GridPane generalPane = new GridPane();
        Text txFiller = new Text("\n\n\n");

        //High score and total stars collected
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
        lbStars.setMinWidth(200 * definingSize);
        TextField tfStars = new TextField(IOMethods.getTotalStarsCollected() + "");
        tfStars.setStyle("-fx-background-color: lavender;\n-fx-stroke-line-join: miter;\n-fx-border-color: black;\n-fx-border-width: 1.8;\n-fx-font-size: 20px;");
        tfStars.setAlignment(Pos.CENTER);
        tfStars.setEditable(false);
        vBox1.getChildren().addAll(lbScore, tfScore, lbStars, tfStars);
        generalPane.add(vBox1, 0, 1);

        //Start, tutorial, settings and exit
        VBox vBox2 = new VBox();
        vBox2.setAlignment(Pos.BOTTOM_CENTER);
        vBox2.setSpacing(20);

        //Start button
        Button btStart = new Button("Start");
        btStart.setPrefWidth(250);
        btStart.setPrefHeight(30);

        //Tutorial button *Tutorial refers to the projectile physics menu*
        Button btTutorial = new Button("Projectile Physics");
        btTutorial.setPrefWidth(250);
        btTutorial.setPrefHeight(30);

        //Settings button
        Button btSettings = new Button("Settings");
        btSettings.setPrefWidth(250);
        btSettings.setPrefHeight(30);

        //Exit button
        Button btExit = new Button("Exit");
        btExit.setPrefWidth(250);
        btExit.setPrefHeight(30);

        vBox2.getChildren().addAll(txFiller, btStart, btTutorial, btSettings, btExit);
        generalPane.add(vBox2, 1, 1);

        //Planets discovered
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
        for (int i = 0; i < 8; i++) {
            double ratio = definingSize;
            VBox vbPlanet = new VBox();
            vbPlanet.setAlignment(Pos.CENTER);
            vbPlanet.setSpacing(5 * ratio);
            ImageView planetImage = new ImageView("file:" + planetArray.get(i).toString() + ".png");
            planetImage.setFitWidth(planetArray.get(i).getSize() * ratio);
            planetImage.setFitHeight(planetArray.get(i).getSize() * ratio);
            ColorAdjust blackout = new ColorAdjust();
            blackout.setBrightness(-1);
            planetImage.setTranslateX(planetArray.get(i).getSetTranslateX() * ratio);

            //If planet is not discovered
            if (isBlackedOut(i)) {
                planetImage.setEffect(blackout);
                Label lbPlanet = new Label("???");
                lbPlanet.setTranslateX(planetArray.get(i).getSetTranslateX() * ratio);
                lbPlanet.setTranslateY(planetArray.get(i).getSetTranslateY() * ratio);
                vbPlanet.getChildren().addAll(planetImage, lbPlanet);
            }

            //If planet is discovered
            else {
                Tooltip tooltipPlanet = new Tooltip("Gravitational acceleration\nof " + planetArray.get(i).toString() + " : " + Math.round(planetArray.get(i).gravity / -1.5551 / definingSize) / 100.0 + " m/s²");
                tooltipPlanet.setTextAlignment(CENTER);
                Tooltip.install(planetImage, tooltipPlanet);
                Label lbPlanet = new Label(planetArray.get(i).toString());
                lbPlanet.setTranslateX(planetArray.get(i).getSetTranslateX() * ratio);
                lbPlanet.setTranslateY(planetArray.get(i).getSetTranslateY() * ratio);
                vbPlanet.getChildren().addAll(planetImage, lbPlanet);
            }
            //Add to gridpane
            gridPane.add(vbPlanet, i % 4, i / 4);
        }
        vBox3.getChildren().addAll(txFiller, lbPlanets, gridPane);
        generalPane.add(vBox3, 2, 1);

        //Setting page
        BorderPane borderPane1 = new BorderPane();
        borderPane1.setStyle("-fx-background-color: rgb(" + 203+ "," + 205 + ", " + 236 + ");");

        //Action
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

        //Control
        VBox vbox2 = new VBox();
        vbox2.setAlignment(Pos.CENTER);
        vbox2.setSpacing(43);
        vbox2.setPadding(new Insets(0, 0, 20, 0));
        Label lbControls = new Label("CONTROL:");
        lbControls.setFont(new Font(40));
        lbControls.setTranslateY(2.5);

        TextField tfJump = new TextField("SPACEBAR");
        tfJump.setFont(new Font(20));
        tfJump.setAlignment(Pos.CENTER);
        tfJump.setEditable(false);
        tfJump.setStyle("-fx-background-color: lavender;\n-fx-stroke-line-join: miter;\n-fx-border-color: black;\n-fx-border-width: 1.8;\n-fx-font-size: 20px;");

        TextField tfThrow = new TextField("RIGHT CLICK");
        tfThrow.setFont(new Font(20));
        tfThrow.setAlignment(Pos.CENTER);
        tfThrow.setTranslateY(-3);
        tfThrow.setEditable(false);
        tfThrow.setStyle("-fx-background-color: lavender;\n-fx-stroke-line-join: miter;\n-fx-border-color: black;\n-fx-border-width: 1.8;\n-fx-font-size: 20px;");

        TextField tfExit = new TextField("ESCAPE");
        tfExit.setFont(new Font(20));
        tfExit.setAlignment(Pos.CENTER);
        tfExit.setTranslateY(-5);
        tfExit.setEditable(false);
        tfExit.setStyle("-fx-background-color: lavender;\n-fx-stroke-line-join: miter;\n-fx-border-color: black;\n-fx-border-width: 1.8;\n-fx-font-size: 20px;");

        TextField tfAim = new TextField("MOUSE");
        tfAim.setFont(new Font(20));
        tfAim.setAlignment(Pos.CENTER);
        tfAim.setTranslateY(-7);
        tfAim.setEditable(false);
        tfAim.setStyle("-fx-background-color: lavender;\n-fx-stroke-line-join: miter;\n-fx-border-color: black;\n-fx-border-width: 1.8;\n-fx-font-size: 20px;");

        vbox2.getChildren().addAll(lbControls, tfJump, tfThrow, tfExit, tfAim);
        borderPane1.setCenter(vbox2);

        //Sound setting
        VBox vbox3 = new VBox();
        vbox3.setAlignment(Pos.CENTER);
        vbox3.setSpacing(30);
        vbox3.setPadding(new Insets(0, 150, 8, 150));
        Label lbSound = new Label("SOUND SETTING:");
        lbSound.setFont(new Font(40));
        lbSound.setTranslateY(-15);

        //Music
        Label lbMusic = new Label("Music:");
        lbMusic.setFont(new Font(30));
        lbMusic.setPadding(new Insets(25, 0, 0, 0));
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        Slider slMusic = new Slider();
        slMusic.setMin(0);
        slMusic.setMax(100);
        slMusic.setValue(50);
        Label lbSlider = new Label((int) slMusic.getValue() + "%");
        lbSlider.setFont(new Font(20));
        lbSlider.setMinWidth(50);
        lbSlider.setMinHeight(50);
        lbSlider.setTranslateX(10);
        slMusic.valueProperty().addListener((observable, oldValue, newValue) -> {
            lbSlider.setText(newValue.intValue() + "%");
            musicSliderValue = newValue.intValue();
            mediaPlayer.setVolume(((double) musicSliderValue) / 100.0);
        });
        hbox.getChildren().addAll(slMusic, lbSlider);


        StackPane mediaAndVisuals = new StackPane();

        //Reset button
        Button btReset = new Button("Reset Files");
        btReset.setPrefWidth(250);
        btReset.setPrefHeight(30);
        setButtonStyle(btReset);
        btReset.setOnAction(e -> {
            IOMethods.reset();
            try {
                start(primaryStage);
            } catch (IOException ex) {
                System.out.println("Error, files corrupted");
            }
        });

        vbox3.getChildren().addAll(lbSound, lbMusic, hbox, btReset);
        borderPane1.setRight(vbox3);


        //Main buttons action handler
        btStart.setOnAction(e -> startGameLoop(primaryStage));
        setButtonStyle(btStart);
        btTutorial.setOnAction(e -> showTutorial(primaryStage));
        setButtonStyle(btTutorial);
        setButtonStyle(btSettings);
        btExit.setOnAction(e -> System.exit(0));
        setButtonStyle(btExit);


        //Music for menu screen
        Media media;
        MediaView mv;
        Pane pane = new Pane();

        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
            File file = new File("MenuMusic.mp3");
            media = new Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mv = new MediaView();
            mediaPlayer.setVolume(((double) musicSliderValue) / 100.0);
            pane.getChildren().add(mv);
            mv.setMediaPlayer(mediaPlayer);
            mediaPlayer.play();
            mediaPlayer.setAutoPlay(true);

        } catch (NullPointerException e) {
            System.out.print("Error NULL for MediaPlayer");
        } catch (Exception e) {
            System.out.print("Error for MediaPlayer");
        }

        //Scene
        generalPane.setHgap(125);
        generalPane.setVgap(100);
        generalPane.setAlignment(Pos.CENTER);
        Image menuImage = new Image("file:menuScreen.bmp");
        ImageView ivBackground = new ImageView(menuImage);
        ivBackground.setFitHeight(screenHeight);
        ivBackground.setFitWidth(screenWidth);
        pane.getChildren().add(ivBackground);
        mediaAndVisuals.getChildren().add(pane);
        mediaAndVisuals.getChildren().add(generalPane);

        Scene scene = new Scene(mediaAndVisuals, screenWidth, screenHeight);
        scene.getStylesheets().add("menuCSS.css");
        mediaAndVisuals.requestFocus();

        //Back button
        BorderPane borderPane2 = new BorderPane();
        borderPane2.setPadding(new Insets(-50, 50, 50, 50));
        Button btBack = new Button("Back");
        btBack.setFont(new Font(20));
        btBack.setOnAction(e -> primaryStage.setScene(scene));
        setButtonStyle(btBack);
        borderPane2.setLeft(btBack);
        borderPane1.setBottom(borderPane2);

        Scene scene2 = new Scene(borderPane1, screenWidth, screenHeight);
        scene2.getStylesheets().add("menuCSS.css");

        btSettings.setOnAction(e -> primaryStage.setScene(scene2));
        primaryStage.setTitle("AstroJump");

        //Listener for back button
        scene2.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                primaryStage.setScene(scene);
            }
        });

        //Create player
        createPlayer();

        //Initialize star
        initializeStar();

        //Create background
        createBackground();

        //Set scene and show stage
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    //BUTTON SET STYLES
    protected void setButtonStyle(Button btObject) {
        btObject.setStyle("-fx-background-color: lavender;\n-fx-stroke-line-join: miter;\n-fx-border-color: black;\n-fx-border-width: 1.8;\n-fx-font-size: 20px;");
        btObject.setOnMousePressed(e -> btObject.setStyle("-fx-background-color: lavender;\n-fx-stroke-line-join: miter;\n-fx-border-color: black;\n-fx-border-width: 2.6;\n-fx-font-size: 22px;"));
        btObject.setOnMouseReleased(e -> btObject.setStyle("-fx-background-color: lavender;\n-fx-stroke-line-join: miter;\n-fx-border-color: black;\n-fx-border-width: 1.8;\n-fx-font-size: 20px;"));
    }

    //SHOW SETTING-TUTORIAL
    protected void showTutorial(Stage primaryStage) {
        //Create scene and add tutorial page according to currentTutorialInt
        ImageView tutorialPage = new ImageView("Page" + (currentTutorialInt + 1) + ".png");
        tutorialPage.setFitWidth(screenWidth);
        tutorialPage.setFitHeight(screenHeight);
        Scene scene = new Scene(new Pane(tutorialPage));
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.requestFocus();

        //Add listener to go back to main menu or switch slides in tutorial
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                try {
                    start(primaryStage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (event.getCode() == KeyCode.RIGHT && currentTutorialInt < 3) {
                currentTutorialInt++;
                showTutorial(primaryStage);
            }
            if (event.getCode() == KeyCode.LEFT && currentTutorialInt > 0) {
                currentTutorialInt--;
                showTutorial(primaryStage);
            }
        });
    }

    //GAMELOOP METHODS
    protected void startGameLoop(Stage primaryStage) {
        //Reset player
        player.setStarsCaught(0);
        player.getAnimation().setRate(1.0);

        //Add first planet to planetsDiscovered
        if (!IOMethods.getPlanetsDiscovered().isEmpty()) {
            planetsDiscovered = IOMethods.getPlanetsDiscovered();
        }
        planetsDiscovered = IOMethods.getPlanetsDiscovered();

        //Add planet to planetsDiscovered
        if (planetsDiscovered.charAt(currentPlanetInt) == '0') {
            planetsDiscovered.setCharAt(currentPlanetInt, '1');
        }

        //Start all timers
        stopAnimationTimer = false;

        //Game scene setup reset
        objectSpeed = -500;
        windDeceleration = 0;
        score = 0;
        if (gameObjects != null)
            gameObjects.getChildren().clear();

        //Change media for music
        mediaPlayer.stop();
        File file = new File("InGameMusic.mp3");
        Media media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(((double) musicSliderValue) / 100.0);
        mediaPlayer.play();
        mediaPlayer.setAutoPlay(true);

        //Initialise txGameInfo
        txGameInfo = new Text("Current Planet: " + planetArray.get(currentPlanetInt).toString() + "\nCurrent Gravity: " + Math.round(planetArray.get(currentPlanetInt).gravity / -1.5551 / definingSize) / 100.0 + " m/s*s\nScore: " + score + "\nStars: " + player.getStarsCaught());
        txGameInfo.setFont(Font.font("Copperplate Gothic Bold", FontWeight.NORMAL, FontPosture.REGULAR, 15 * definingSize));
        txGameInfo.setFill(Color.WHITE);
        txGameInfo.setStrokeWidth(.8 * definingSize);
        txGameInfo.setStroke(Color.BLACK);
        txGameInfo.setTextAlignment(LEFT);
        txGameInfo.setX(6 * definingSize);
        txGameInfo.setY(22 * definingSize);

        //Initialise txGameOver
        txGameOver = new Text("");
        txGameOver.setFont(Font.font("Copperplate Gothic Bold", FontWeight.NORMAL, FontPosture.REGULAR, 20 * definingSize));
        txGameOver.setFill(Color.WHITE);
        txGameOver.setStrokeWidth(0.8 * definingSize);
        txGameOver.setStroke(Color.BLACK);
        txGameOver.setTextAlignment(CENTER);
        BorderPane pane = new BorderPane();
        pane.setPrefSize(screenWidth, screenHeight);
        pane.setCenter(txGameOver);

        //Initialise net info text
        txNetInfo = new Text("");
        txNetInfo.setFont(Font.font("Copperplate Gothic Bold", FontWeight.NORMAL, FontPosture.REGULAR, 15 * definingSize));
        txNetInfo.setFill(Color.WHITE);
        txNetInfo.setStrokeWidth(.8 * definingSize);
        txNetInfo.setStroke(Color.BLACK);
        txNetInfo.setTextAlignment(LEFT);
        txNetInfo.setX(screenWidth - 275 * definingSize);
        txNetInfo.setY(20 * definingSize);
        txNetInfo.setTextAlignment(TextAlignment.RIGHT);

        //Initialise gameObjects
        gameObjects = new Group(background.getImage(), player.getImage(), star.getImage(), txGameInfo, txNetInfo, pane);


        //Clear nets, obstacles
        nets.clear();
        obstacles.clear();

        //Reset timers
        obstacleSpawnIntervalNano = (long) 3.5 * 1_000_000_000;
        starSpawnIntervalNano = (long) 4 * 1_000_000_000;
        portalSpawnIntervalNano = (long) 30 * 1_000_000_000;

        Scene game = new Scene(gameObjects, screenWidth, screenHeight);

        //Initialize the next star spawning
        randomizeStarSpawnTime();

        //Event handlers on scene
        //Escape to main menu handler
        game.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                try {
                    start(primaryStage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        //Game restart handler
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

        //Jump event handler
        game.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE) {
                if (player.getY() + player.getHeight() >= GROUND_Y - screenHeight / 12 && player.getJumpTimeElapsed() > 0.4) {
                    player.setIsJumping(true);
                    player.setAnimationState(Player.JUMPING);
                } else if (!player.getIsJumping()) {
                    player.setIsJumping(true);
                    player.setY(GROUND_Y - 1 - player.getHeight());
                    player.setAnimationState(Player.JUMPING);
                }
            }
        });

        //Create net when mouse released
        game.setOnMouseReleased(event -> {
            parabolaPath.getElements().clear();
            txNetInfo.setText("");
            updatePath = false;
            createNet(planetArray.get(currentPlanetInt).getGravity(), NET_FORCE);
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

        //Set scene and show stage
        primaryStage.setScene(game);
        primaryStage.show();

        //Create animationTimer for update method and gameObjectSpawner
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Calculate the time elapsed since the last frame
                if (stopAnimationTimer) {
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

    //UPDATE METHOD
    private void update(double deltaTime, Stage stage) {
        //DeltaTime is the time elapsed since the last frame in seconds; you can multiply a value of speed or position by deltaTime to make it pixels/second
        //Increase object speeds
        objectSpeed += (float) (objectSpeed * 0.005 * deltaTime);
        updateGameObjectsSpeed();

        //Increase wind acceleration on nets
        windDeceleration += (float) (deltaTime * definingSize * -1.5);

        //Update txGameInfo
        txGameInfo.setText("Current Planet: " + planetArray.get(currentPlanetInt).toString() + "\nCurrent Gravitiy: " + Math.round(planetArray.get(currentPlanetInt).gravity / -1.5551 / definingSize) / 100.0 + " m/s²\nScore: " + score + "\nStars: " + player.getStarsCaught());

        //Update PLAYER jump
        if (player.getIsJumping()) {
            playerJump(planetArray.get(currentPlanetInt).getGravity(), PLAYER_JUMP_FORCE);
        }

        //Update is on ground boolean
        player.updateIsOnGround(GROUND_Y);

        //Net updates
        for (int i = 0; i < nets.size(); i++) {
            Net net = nets.get(i);

            //Update position
            net.updatePosition();

            //Check for collision with a star
            if (star != null) {
                if (net.isCollidingWith(star.getImage())) {
                    player.addOneStar();
                    //modify score
                    score += (long) star.getScoreValue();
                    //reset star
                    spawnStar(screenWidth, 0, 0, 0, 0);
                }
            }
            //Check for collision with ground
            if (net.getY() + net.getHeight() >= GROUND_Y) {
                //Delete game object
                gameObjects.getChildren().remove(net.getImage());
                nets.remove(net);
                net = null;
                i--;
            }
        }

        //OBSTACLES UPDATE
        for (int i = 0; i < obstacles.size(); i++) {
            SimpleMovingImage obstacle = obstacles.get(i);

            //Update position
            obstacle.updatePosition(deltaTime);

            //Check for collisions with player
            if (obstacle.isCollidingWith(player.getImage())) {
                //Game over methods
                IOMethods saveData = new IOMethods(score, player.getStarsCaught(), planetsDiscovered);
                stopAnimationTimer = true;
                player.setAnimationState(Player.DEAD);
                txGameOver.setText("GAME OVER\nPRESS ESCAPE TO EXIT TO MAIN MENU OR R TO RESTART");
                mediaPlayer.stop();
                //ShowGameOverScreen(stage);
            }

            //If the obstacle is out of bounds delete it
            if (obstacle.isOutOfBounds(GROUND_Y)) {
                gameObjects.getChildren().remove(obstacle.getImage());
                obstacle = null;
                obstacles.remove(i);
                i--;
            }

            //Check for collision with portal
            if (portal != null & obstacle != null) {
                if (obstacle.isCollidingWith(portal.getImage())) {
                    //Delete obstacle
                    gameObjects.getChildren().remove(obstacle.getImage());
                    obstacle = null;
                    obstacles.remove(i);
                    i--;
                }
            }

            //Make the player's animation faster
            player.getAnimation().setRate(player.getAnimation().getRate() + deltaTime / 100.0);
        }

        //UPDATE STAR
        star.updatePosition(deltaTime);

        if (star.isCollidingWith(player.getImage())) {
            player.addOneStar();
            score += (long) star.getScoreValue();
            //Reset star
            spawnStar(screenWidth, 0, 0, 0, 0);
        }
        //Reset the star if it is out of bounds
        if (star.isOutOfBounds(GROUND_Y)) {
            spawnStar(screenWidth, 0, 0, 0, 0);
        }

        //Background update
        background.updatePosition(deltaTime);

        //UPDATE PORTAL
        if (portal != null) {
            //Update position
            portal.updatePosition(deltaTime);
            //Out of bounds deletion
            if (portal.isOutOfBounds(GROUND_Y)) {
                //Delete portal
                gameObjects.getChildren().remove(portal.getImage());
                portal = null;
            }
        }
        //Collision detection for portal
        if (portal != null) {
            if (portal.isCollidingWith(player.getImage()) && !player.getIsJumping()) {
                //Change planet
                changePlanet();
                //Update spikes graphics
                updateSpikePlanet();
                //Delete portal
                gameObjects.getChildren().remove(portal.getImage());
                portal = null;
            }
        }

        //UPDATE PATH
        if (updatePath) {
            //Calculate the initial position of the net
            double initialPosX = player.getX() + player.getWidth();
            double initialPosY = player.getY() + (0.5 * player.getHeight()) - (Net.WIDTH * 0.5);

            //Calculate the angle of the throw
            double dx = lastKnownMouseX - initialPosX;
            double dy = -lastKnownMouseY + initialPosY;

            //Angle from player to point (world angle)
            double angle = Math.atan2(dy, dx);

            //Calculate the speed in each axis
            double initialSpeedX = NET_FORCE * Math.cos(angle);
            double initialSpeedY = -NET_FORCE * Math.sin(angle);
            //Get acceleration
            double accelerationY = planetArray.get(currentPlanetInt).getGravity();

            //Draw line
            // Create a path for the parabola
            double k = (Math.pow(initialSpeedY, 2) / (2 * accelerationY)) + initialPosY;
            double h = initialSpeedX * initialSpeedY / accelerationY + initialPosX;

            //Calculate a
            double a = (initialPosY - k) / Math.pow((initialPosX - h), 2);
            parabolaPath = createParabola(parabolaPath, a, h, k, initialPosX);

            //Add parabolaPath if it isn't already there
            if (!gameObjects.getChildren().contains(parabolaPath))
                gameObjects.getChildren().add(parabolaPath);

            //UPDATE NET INFO
            txNetInfo.setText("NET THROW INFO:\n" +
                    "Initial Speed: " + NET_FORCE + "(px/s)" +
                    "\nAngle thrown: " + roundTo2Dec(Math.toDegrees(angle)) + "(degrees)" +
                    "\nInitial Speed X: " + (int) initialSpeedX + "(px/s)" +
                    "\nInitial Speed Y: " + (int) -initialSpeedY + "(px/s)" +
                    "\nAcceleration X: " + roundTo2Dec(windDeceleration) + "(px/s²)" +
                    "\nAcceleration Y: " + roundTo2Dec(accelerationY) + "(px/s²)"
            );
        }


    }

    //PLAYER METHODS
    private void createPlayer() {
        final Image IMAGE = new Image("Character.png");
        //Number of columns in the spriteSheet
        final int COLUMNS = 4;
        final int STARTING_ROW = Player.RUN;
        //Beginning offset
        final int OFFSET_X = 0;
        final int OFFSET_Y = 0;
        //Size of one image
        final int SPRITE_WIDTH = 230;
        final int SPRITE_HEIGHT = 240;
        ImageView playerIV = new ImageView(IMAGE);
        //Set player imageView to first image
        playerIV.setViewport(new Rectangle2D(OFFSET_X, OFFSET_Y, SPRITE_WIDTH, SPRITE_HEIGHT));
        playerIV.setFitWidth(PLAYER_WIDTH);
        playerIV.setFitHeight(PLAYER_HEIGHT);

        //Animation player imageview
        final Duration PLAYER_ANIM_DURATION = Duration.millis(1000);
        Sprite playerAnimation = new Sprite(
                playerIV,
                PLAYER_ANIM_DURATION,
                STARTING_ROW, COLUMNS,
                OFFSET_X, OFFSET_Y,
                SPRITE_WIDTH, SPRITE_HEIGHT
        );
        playerAnimation.setCycleCount(Animation.INDEFINITE);
        playerAnimation.play();

        //Create player object
        player = new Player(playerIV, playerAnimation);
        player.setAnimationState(Player.RUN);
        player.setY(GROUND_Y - player.getHeight());
        player.setX(0);

    }

    private void playerJump(float gravitationalForce, float initialJumpSpeed) {
        //Set player animation to jump
        //Move player
        double timeElapsed = player.getJumpTimeElapsed();
        double baseDisplacement = timeElapsed * initialJumpSpeed;
        double acceleratedDisplacement = -0.5 * gravitationalForce * Math.pow(timeElapsed, 2);

        player.setY(GROUND_Y - player.getHeight() + baseDisplacement + acceleratedDisplacement);

        //If the player is falling set state to falling
        if (gravitationalForce * timeElapsed + initialJumpSpeed < 0)
            player.setAnimationState(Player.FALLING);

        //If the player is back on the floor set is jumping to false
        if (player.getY() >= (GROUND_Y - player.getHeight())) {
            player.setIsJumping(false);

            //Adjust player y values to ground
            player.setY(GROUND_Y - player.getHeight());
        }
    }

    //NET METHOD
    public void createNet(float gravity, float netForce) {
        //Calculate the inital position of the net
        double initialPosX = player.getX() + player.getWidth();
        double initialPosY = player.getY() + (0.5 * player.getHeight()) - (0.5 * NET_SIZE);

        //Calculate the angle of the throw
        double dx = lastKnownMouseX - initialPosX;
        double dy = -lastKnownMouseY + initialPosY;

        //Angle from player to point (world angle)
        double angle = Math.atan2(dy, dx);

        //Calculate the speed in each axis
        double initialSpeedX = netForce * Math.cos(angle);
        double initialSpeedY = -netForce * Math.sin(angle);

        //If the player is aiming backwards shot the net like a slingshot
        if (initialSpeedX < 0) {
            initialSpeedX *= -1;
            initialSpeedY *= -1;
        }
        //Create net

        //Adds a new net to the nets array
        nets.add(new Net(new ImageView("Net.png"), initialPosX, initialPosY, initialSpeedX, initialSpeedY, gravity, windDeceleration));
        //Adds the last net added to nets to the game object group
        gameObjects.getChildren().add(nets.getLast().getImage());
        //Set its initial Positions to the nets initial positions
        nets.getLast().setX(initialPosX);
        nets.getLast().setY(initialPosY);
        nets.getLast().setHeight(NET_SIZE);
        nets.getLast().setWidth(NET_SIZE);


    }

    //OBSTACLE METHODS
    public void createSpike() {
        //Add new obstacle
        obstacles.add(new SimpleMovingImage(new ImageView("Obstacle.png"), SPIKE_WIDTH, SPIKE_HEIGHT, objectSpeed, 0));

        //Change image view
        ImageView imgV = obstacles.getLast().getImage();
        SimpleMovingImage obstacle = obstacles.getLast();

        //Set to the right spike image (random spike on the current planet)
        imgV.setViewport(new Rectangle2D((int) (Math.random() * 5) * 210, currentPlanetInt * 280, 210, 280));

        //Add imageView to game objects
        gameObjects.getChildren().add(imgV);

        //Set obstacle to the right position
        obstacle.setY(GROUND_Y - obstacle.getHeight());
        obstacle.setX(screenWidth);//CHECK
    }

    public void createMeteorite() {
        //Add new meteorite
        obstacles.add(new SimpleMovingImage(new ImageView("Meteorite.png"), METEOR_WIDTH, METEOR_HEIGHT, objectSpeed, 0));

        //Set obstacle to the right position
        SimpleMovingImage obstacle = obstacles.getLast();

        //Change image view
        ImageView imgV = obstacle.getImage();
        imgV.setViewport(new Rectangle2D((Math.round(Math.random())) * 320, 0, 320, 100));

        obstacle.setY(Math.random() * (GROUND_Y - obstacle.getHeight() - player.getHeight() - 10));
        obstacle.setX(screenWidth);

        //Add imageView to game objects
        gameObjects.getChildren().add(obstacles.getLast().getImage());
    }

    public void updateSpikePlanet() {
        for (SimpleMovingImage obstacle : obstacles) {
            //Make sure the object is a spike
            if (obstacle.getHeight() == SPIKE_HEIGHT && obstacle.getWidth() == SPIKE_WIDTH) {
                //Change the viewport to the right planet
                //Set to the right spike image (same spike on the current planet)
                Rectangle2D oldViewport = obstacle.getImage().getViewport();
                obstacle.getImage().setViewport(new Rectangle2D(oldViewport.getMinX(), currentPlanetInt * 280, 210, 280));
            }
        }
    }

    //STAR METHODS
    public void initializeStar() {
        //Create star
        star = new Star(new ImageView("Star.png"), STAR_WIDTH, STAR_HEIGHT, 0, 0, 0);

        //Get random index 0-1
        int index = (int) (Math.round(Math.random()));

        //Randomize the star image
        star.getImage().setViewport(new Rectangle2D(index * 230, 0, 230, 230));

        //Set position and size
        star.setX(screenWidth);
    }

    public void spawnStar(double x, double y, float speedX, float speedY, double scoreValue) {
        star.setX(x);
        star.setY(y);
        star.setSpeedX(speedX);
        star.setSpeedY(speedY);
        star.setScoreValue(scoreValue);

        //Get random index 0-1
        int index = (int) (Math.round(Math.random()));
        //Randomize the star image
        star.getImage().setViewport(new Rectangle2D(index * 230, 0, 230, 230));
    }

    //BACKGROUND METHOD
    public void createBackground() {
        background = new Background(new ImageView("Background.png"), BACKGROUND_WIDTH, BACKGROUND_HEIGHT, 5120, 1280, objectSpeed, currentPlanetInt);
    }

    //PATH METHOD
    private Path createParabola(Path path, double a, double h, double k, double startX) {
        //Reset the path is if it doesn't exist already
        if (path == null)
            path = new Path();
        else
            path.getElements().clear();

        //Style the path as a white dotted line
        path.setStroke(Color.WHITE);
        path.setStrokeWidth(2);
        path.getStrokeDashArray().addAll(5.0, 5.0); //This creates the dotted effect
        path.setFill(null); //Important for a line (not filled)

        double endX = screenWidth;    //End at screen width
        double step = 1;          //Smaller step = smoother curve

        //Move to starting point
        double startY = a * Math.pow(startX - h, 2) + k + Net.WIDTH * 0.5;
        path.getElements().add(new MoveTo(startX, startY));

        //Create the parabola by adding line segments
        for (double x = startX + step; x <= endX; x += step) {
            double y = a * Math.pow(x - h, 2) + k + Net.WIDTH * 0.5;
            path.getElements().add(new LineTo(x, y));
        }

        return path;
    }

    //PORTAL METHOD
    private void spawnPortal() {
        //Spawn portal
        portal = new SimpleMovingImage(new ImageView("Black_hole.png"), PORTAL_WIDTH, PORTAL_HEIGHT, objectSpeed, 0);
        portal.setX(screenWidth);
        portal.setY(GROUND_Y - portal.getHeight());
        gameObjects.getChildren().add(portal.getImage());
    }

    //PLANET CHANGE METHOD
    private void changePlanet() {
        //Determine new planet and update currentPlanetInt
        int newPlanetInt = (int) Math.round((Math.random() * 7));
        while (currentPlanetInt == newPlanetInt) {
            newPlanetInt = (int) Math.round((Math.random() * 7));
        }
        currentPlanetInt = newPlanetInt;
        //Update background
        background.changePlanet(currentPlanetInt);
        //Add planet to planetsDiscovered
        if (planetsDiscovered.charAt(currentPlanetInt) == '0') {
            planetsDiscovered.setCharAt(currentPlanetInt, '1');
        }
    }

    //GAMEPLAY SPAWNING METHODS
    private void gameObjectSpawner(long now) {

        //Initialize spawning time for star and portal
        if (startSpawnPortalStarTime == 0) {
            startSpawnPortalStarTime = now + 1_000_000_000;
            lastPortalSpawnTime = now;
            lastStarSpawnTime = now;
        }

        //Spawn a new obstacle if the spawn interval has passed
        if (now - lastObstacleSpawnTime >= obstacleSpawnIntervalNano) {
            spawnObstacle();
            lastObstacleSpawnTime = now;
            increaseSpawnSpeed(); // Gradually increase spawn speed
        }
        //Spawn a new star if the spawn interval has passed
        if (now - lastStarSpawnTime >= starSpawnIntervalNano && now > startSpawnPortalStarTime) {
            //Give a score value based on time passed
            long scoreValue = (long) 500 * 1_000_000_000 / obstacleSpawnIntervalNano;

            spawnStar(Math.random() * (screenWidth - star.getWidth()), Math.random() * (GROUND_Y - star.getHeight()), 0, 0, scoreValue);

            lastStarSpawnTime = now;
            randomizeStarSpawnTime(); //Gradually increase spawn speed
        }

        //Spawn a new portal if the spawn interval has passed
        if (now - lastPortalSpawnTime >= portalSpawnIntervalNano && now > startSpawnPortalStarTime) {
            spawnPortal();

            lastPortalSpawnTime = now;
            randomizePortalSpawnTime(); //Gradually increase spawn speed
        }


    }

    private void spawnObstacle() {
        //Create new random obstacle
        int index = (int) (Math.round(Math.random()));
        switch (index) {
            case 0: //Spawn spike
                createSpike();
                break;
            case 1: //Spawn meteor
                createMeteorite();
                break;
        }
    }

    public void randomizeStarSpawnTime() {
        starSpawnIntervalNano = (long) (((Math.random() * 5) + 10) * 1_000_000_000);
    }

    public void randomizePortalSpawnTime() {
        portalSpawnIntervalNano = (long) (((Math.random() * 10) + 20) * 1_000_000_000);
    }

    private void increaseSpawnSpeed() {
        //Decrease the spawn interval
        long SPAWN_TIME_DECREMENT = 20_000_000;
        obstacleSpawnIntervalNano = Math.max(300_000_000, obstacleSpawnIntervalNano - SPAWN_TIME_DECREMENT);// Don't go below 0.2 seconds
    }

    private void updateGameObjectsSpeed() {
        //Update background
        background.setSpeedX(objectSpeed);
        //Update obstacles
        for (SimpleMovingImage obstacle : obstacles) {
            obstacle.setSpeedX(objectSpeed);
        }

    }

    //IsBlackedOut method
    public boolean isBlackedOut(int planetInt) {
        return IOMethods.getPlanetsDiscovered().charAt(planetInt) == '0';
    }

    //MATH METHOD
    private double roundTo2Dec(double value) {
        return ((int) (value * 100)) / 100.0;
    }
}
