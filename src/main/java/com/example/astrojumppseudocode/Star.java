package com.example.astrojumppseudocode;

import javafx.scene.image.ImageView;

public class Star extends SimpleMovingImage{
    private double ScoreValue;
    Star(ImageView img,int width, int height,float speedX,float speedY,double scoreValue){
        super(img,width,height,speedX,speedY);
        this.ScoreValue = scoreValue;
    }

    //scoreValue accessors and mutators
    public void setScoreValue(double scoreValue){
        this.ScoreValue=scoreValue;
    }
    public double getScoreValue(){
        return this.ScoreValue;
    }


}
