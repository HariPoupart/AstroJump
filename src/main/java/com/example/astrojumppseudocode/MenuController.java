package com.example.astrojumppseudocode;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

public class MenuController {
    AstroJump game;

    //action handler startButton
    @FXML
    protected static void startButton() {
        // Start the game loop
        AstroJump.startLoopListener.setValue(true);
}

    //action handler tutorialButton
    @FXML
    public static void tutorialButton() {
        AstroJump.tutorialListener.setValue(true);
    }

    //action handler settingButton
    @FXML
    public static void settingButton() {
        AstroJump.settingsListener.setValue(true);
    }

    //action handler settingButton
    @FXML
    public static void exitButton() {
        System.exit(0);
    }

}
