package com.example.astrojumppseudocode;

import javafx.scene.image.ImageView;

public class Net implements Collidable{
    //general
    private ImageView image;
    private double width;
    private double height;

    //motion
    private final double INITIAL_SPEED_X;
    private final double INITIAL_SPEED_Y;
    private final double INITIAL_POS_X;
    private final double INITIAL_POS_Y;
    private float accelerationY;
    private float accelerationX;
    private final long CREATION_TIME;

    Net(ImageView img,double initialPositionX, double initialPositionY,double initialSpeedX,double initialSpeedY, float gravity, float accelerationX){
        this.image = img;
        this.width = img.getFitWidth();
        this.height = img.getFitHeight();

        this.INITIAL_POS_X = initialPositionX;
        this.INITIAL_POS_Y = initialPositionY;
        this.INITIAL_SPEED_X = initialSpeedX;
        this.INITIAL_SPEED_Y = initialSpeedY;
        this.accelerationY = gravity;
        this.accelerationX = accelerationX;

        this.CREATION_TIME = System.currentTimeMillis();
    }

    //position accessors and mutators
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
    public void updatePosition(){
        //update x
        double baseDisplacementX = getTimeElapsed()*INITIAL_SPEED_X;
        double acceleratedDisplacementX = -0.5*accelerationX*Math.pow(getTimeElapsed(),2);
        setX(INITIAL_POS_X+baseDisplacementX+acceleratedDisplacementX);

        //update y
        double baseDisplacementY = getTimeElapsed()*INITIAL_SPEED_Y;
        double acceleratedDisplacementY = -0.5*accelerationY*Math.pow(getTimeElapsed(),2);
        setY(INITIAL_POS_Y+baseDisplacementY+acceleratedDisplacementY);
    }

    //acceleration accessors and mutators
    public void setAccelerationX(float accelerationX){
        this.accelerationX=accelerationX;
    }
    public void setAccelerationY(float accelerationY){
        this.accelerationY=accelerationY;
    }
    public float getAccelerationX(){
        return this.accelerationX;
    }
    public float getAccelerationY(){
        return this.accelerationY;
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
    public ImageView getImage(){
        return this.image;
    }

    //check if this works
    public boolean isCollidingWith(ImageView other){
        boolean isColliding = false;
        if(image.getBoundsInParent().intersects(other.getBoundsInParent())){
            isColliding = true;
        }
        return isColliding;
    }

    public long getCREATION_TIME(){
        return this.CREATION_TIME;
    }
    public double getTimeElapsed(){
        return (System.currentTimeMillis()-this.CREATION_TIME)/1000;
    }

}
