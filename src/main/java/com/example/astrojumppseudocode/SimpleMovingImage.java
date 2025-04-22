package com.example.astrojumppseudocode;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SimpleMovingImage implements Collidable{
    private ImageView image;
    private double width;
    private double height;
    private float speedX;
    private float speedY;

    SimpleMovingImage(ImageView img,int width, int height, float speedX, float speedY){
        this.image = img;
        this.speedX = speedX;
        this.speedY = speedY;
        this.setWidth(width);
        this.setHeight(height);
    }
    //position & speed accessors and mutators
    public void setX(double x){
        image.setX(x);
    }
    public void setY(double y){
        image.setY(y);
    }
    public double getX(){
        return image.getX();
    }
    public double getY(){
        return image.getY();
    }
    public void setSpeedX(float speedX){
        this.speedX = speedX;
    }
    public float getSpeedX(){
        return this.speedX;
    }
    public void setSpeedY(float speedY){
        this.speedY = speedY;
    }
    public float getSpeedY(){
        return this.speedY;
    }
    //accessors and mutators for width & height
    public void setHeight(double height){
        this.height = height;
        this.image.setFitHeight(height);
    }
    public void setWidth(double width){
        this.width = width;
        this.image.setFitWidth(width);
    }
    public double getHeight(){
        return this.height;
    }
    public double getWidth(){
        return this.width;
    }
    public void setImage(ImageView imageView){
        this.image = imageView;
    }
    public void setImage(Image image){
        this.image.setImage(image);
    }
    public ImageView getImage(){
        return this.image;
    }
    public void updatePosition(double deltaTime){
        //update x
        setX(getX()+speedX*deltaTime);
        //update y
        setY(getY()+speedY*deltaTime);
    }
    public boolean isCollidingWith(ImageView other){
        return this.getImage().getBoundsInParent().intersects(other.getBoundsInParent());
    }
    public boolean isOutOfBounds(double groundLevel){
        return (getX() + getWidth() <= 0 || getY() >= groundLevel);
    }
}
