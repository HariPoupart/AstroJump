package com.example.astrojumppseudocode;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class SimpleMovingImage {
    private ImageView image;
    private double width;
    private double height;
    private float speedX;
    private float speedY;

    SimpleMovingImage(ImageView img, float speedX, float speedY){
        this.image = img;
        this.width = img.getFitWidth();
        this.height = img.getFitHeight();
        this.speedX = speedX;
        this.speedY = speedY;
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
    public void updatePosition(){
        final int frameRate = 60;
        //update x
        setX(getX()+speedX/frameRate);
        //update y
        setY(getY()+speedY/frameRate);
    }
}
