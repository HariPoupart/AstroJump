package com.example.astrojumppseudocode;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Planet {
    //planet attributes
    float gravity;
    float jumpForce;


    //constructor
    Planet(float gravity, float jumpForce) {
        this.gravity = gravity;
        this.jumpForce = jumpForce;

    }

    //planet mutators and accessors
    public void setGravity(float gravity) {
        this.gravity = gravity;
    }
    public void setjumpForce(float jumpForce) {
        this.jumpForce = jumpForce;
    }

    public float getGravity() {
        return gravity;
    }
    public float getjumpForce() {
        return jumpForce;
    }


}
