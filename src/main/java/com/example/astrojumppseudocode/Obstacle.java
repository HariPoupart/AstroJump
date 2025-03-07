package com.example.astrojumppseudocode;

import javafx.scene.image.ImageView;

public class Obstacle extends SimpleMovingImage implements Collidable{

    Obstacle(ImageView img,int width, int height,float speedX,float speedY){
        super(img,width,height,speedX,speedY);
    }

    //check if this works
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
