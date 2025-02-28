package com.example.astrojumppseudocode;

import javafx.beans.property.SimpleBooleanProperty;
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
        AstroJump.startLoopListener.setValue(true);
}

    //action handler tutorialButton
    @FXML
    protected void tutorialButton(ActionEvent event) {
        AstroJump.tutorialListener.setValue(true);
    }

    //action handler settingButton
    @FXML
    protected void settingButton(ActionEvent event) {
        AstroJump.settingsListener.setValue(true);
    }

    //action handler settingButton
    @FXML
    protected void exitButton(ActionEvent event) {
        System.exit(0);
    }
}
