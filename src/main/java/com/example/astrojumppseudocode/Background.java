package com.example.astrojumppseudocode;
import javafx.scene.image.ImageView;

public class Background extends SimpleMovingImage {
    //general info
    private final int TOTAL_WIDTH;


    Background(ImageView img,float speedX,int totalWidth){
        super(img,speedX,0);
        this.TOTAL_WIDTH = totalWidth;
    }

    public boolean isHalfway(){
        return (getX() <=-0.5*TOTAL_WIDTH);
    }

}
