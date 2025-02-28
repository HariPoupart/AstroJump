package com.example.astrojumppseudocode;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Planet {
    //planet attributes
    float gravity;
    float jumpForce;
    float netForce;


    //constructor
    Planet(float gravity, float jumpForc, float netForce) {
        this.gravity = gravity;
        this.jumpForce = jumpForce;
        this.netForce = netForce;

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


}
