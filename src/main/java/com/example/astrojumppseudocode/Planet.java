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
    Planet(String name, float gravity, float jumpForce, float netForce, double setTranslateX, double setTranslateY, int size) {
        this.gravity = gravity;
        this.jumpForce = jumpForce;
        this.netForce = netForce;
        this.name = name;
        this.setTranslateX = setTranslateX;
        this.setTranslateY = setTranslateY;
        this.size = size;
    }

    //planet mutators and accessors
    public void setGravity(float gravity) {
        this.gravity = gravity;
    }
    public void setjumpForce(float jumpForce) {
        this.jumpForce = jumpForce;
    }
    public void setNetForce(float netForce) {
        this.netForce = netForce;
    }

    public float getGravity() {
        return gravity;
    }
    public float getjumpForce() {
        return jumpForce;
    }
    public float getNetForce() {
        return netForce;
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
