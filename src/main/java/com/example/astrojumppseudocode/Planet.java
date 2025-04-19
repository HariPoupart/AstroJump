package com.example.astrojumppseudocode;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Planet {
    //planet attributes
    float gravity;
    float jumpForce;
    float netForce;
    String name;
    double setTranslateX;
    double setTranslateY;
    int size;

    //constructor
    Planet(String name, float gravity, double setTranslateX, double setTranslateY, int size) {
        this.gravity = gravity;
        this.name = name;
        this.setTranslateX = setTranslateX;
        this.setTranslateY = setTranslateY;
        this.size = size;
    }

    //planet mutators and accessors
    public void setGravity(float gravity) {
        this.gravity = gravity;
    }
    public float getGravity() {
        return gravity;
    }

    public String toString() {
        return name;
    }
    public double getSetTranslateX() {
        return setTranslateX;
    }
    public double getSetTranslateY() {
        return setTranslateY;
    }
    public int getSize() {
        return size;
    }


}
