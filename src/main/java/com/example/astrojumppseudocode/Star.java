package com.example.astrojumppseudocode;

import javafx.scene.image.ImageView;

public class Star extends SimpleMovingImage implements Collidable{
    private double ScoreValue;
    Star(ImageView img,float speedX,float speedY,double scoreValue){
        super(img,speedX,speedY);
        this.ScoreValue = scoreValue;
    }

    //scoreValue accessors and mutators
    public void setScoreValue(double scoreValue){
        this.ScoreValue=scoreValue;
    }
    public double getScoreValue(){
        return this.ScoreValue;
    }

    /**
     * Checks for collisions
     * @param other other object's imageview
     * @return if they are colliding or not
     */
    public boolean isCollidingWith(ImageView other){
        boolean isColliding = false;
        if(this.getImage().getBoundsInParent().intersects(other.getBoundsInParent())){
            isColliding = true;
        }
        return isColliding;
    }
    public boolean isOutOfBounds(double groundLevel){
        return (getX() + getWidth() <= 0 || getY() >= groundLevel);
    }

}
