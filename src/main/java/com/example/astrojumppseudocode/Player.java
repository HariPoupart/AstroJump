package com.example.astrojumppseudocode;

import javafx.scene.image.ImageView;


public class Player implements Collidable{
    //general
    private ImageView image;
    private Sprite animation;
    private double width;
    private double height;
    private int starsCaught;
    //jumping
    private boolean isOnGround;
    private boolean isJumping;
    private long startJumpTime;

    //animation constants
    public static final int RUN = 0;
    public static final int JUMPING = 1;
    public static final int FALLING = 2;
    public static final int DEAD = 3;


    Player(ImageView img,Sprite animation){
        this.image = img;
        this.animation = animation;
        this.width = img.getFitWidth();
        this.height = img.getFitHeight();
        //default values
        this.isOnGround = false;
        this.isJumping = false;
        this.starsCaught = 0;
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

    //accessors and mutators for image
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

    //stars accessor and mutator
    public void setStarsCaught(int newstarsCaught){
        this.starsCaught=newstarsCaught;
    }
    public void addOneStar(){
        this.starsCaught++;
    }
    public int getStarsCaught(){
        return this.starsCaught;
    }

    //check if this works
    public boolean isCollidingWith(ImageView other){
        return image.getBoundsInParent().intersects(other.getBoundsInParent());
    }

    //animation accessor and mutator
    public void setAnimationState(int animationState){
        this.animation.setRow(animationState);
    }
    public int getAnimationState(){
        return this.animation.getRow();
    }
    public void setAnimation(Sprite animation){
        this.animation = animation;
    }
    public Sprite getAnimation(){
        return this.animation;
    }

    //jumping methods
    public void updateIsOnGround(double floorY){
        this.isOnGround = floorY >= (getY() + this.height);
    }
    public boolean getIsOnGround(){
        return this.isOnGround;
    }

    public void setIsJumping(boolean isJumping){
        this.isJumping = isJumping;
        //if the player starts jumping record time
        if(isJumping)
            this.startJumpTime = System.currentTimeMillis();
        else
            setAnimationState(RUN);
    }
    public boolean getIsJumping(){
        return this.isJumping;
    }

    public long getStartJumpTime(){
        return this.startJumpTime;
    }
    public double getJumpTimeElapsed(){
        return (System.currentTimeMillis()-this.startJumpTime)/1000.0;
    }

}
