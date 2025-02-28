package com.example.astrojumppseudocode;

import javafx.scene.image.ImageView;

public class Planet {
    //planet attributes
    float gravity;
    ImageView background;
    ImageView[] obstacles;

    //constructor
//    Planet() {
//        this.gravity = gravity;
//        this.background = background;
//        this. obstacles = obstacles;
//    }

    //planet mutators and accessors
    public void setGravity(float gravity) {
        this.gravity = gravity;
    }
    public void setBackground(ImageView background) {
        this.background = background;
    }
    public void setObstacles(ImageView[] obstacles) {
        this.obstacles = obstacles;
    }

    public float getGravity() {
        return gravity;
    }
    public ImageView getBackground() {
        return background;
    }
    public ImageView[] getObstacles() {
        return obstacles;
    }


}
