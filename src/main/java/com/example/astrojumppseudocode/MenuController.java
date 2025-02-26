package com.example.astrojumppseudocode;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class MenuController {
    AstroJump game;

    //action handler startButton
    @FXML
    protected void startButton(ActionEvent event) {
        // Start the game loop
        game = new AstroJump();
        game.startGameLoop();
}

    //action handler tutorialButton
    @FXML
    protected void tutorialButton(ActionEvent event) {
        Scene scene = new Scene(new Pane(new ImageView("tutorial.png")),320,240);
    }

    //action handler settingButton
    @FXML
    protected void settingButton(ActionEvent event) {
        Scene scene = new Scene(new Pane(new ImageView("settings.png")),320,240);
    }

    //action handler settingButton
    @FXML
    protected void exitButton(ActionEvent event) {
        System.exit(0);
    }
}
