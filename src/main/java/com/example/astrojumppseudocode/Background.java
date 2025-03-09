package com.example.astrojumppseudocode;
import javafx.scene.image.ImageView;

public class Background extends SimpleMovingImage {
    //general info
    private final int TOTAL_WIDTH;


    Background(ImageView img,int width, int height,float speedX,int totalWidth){
        super(img,width,height,speedX,0);
        this.TOTAL_WIDTH = totalWidth;
    }

    public boolean isHalfway(){
        return (getX() <=-0.5*TOTAL_WIDTH);
    }

    @Override
    public void updatePosition(double deltaTime){
        //update x
        setX(getX()+this.getSpeedX()*deltaTime);
        //update y
        setY(getY()+this.getSpeedY()*deltaTime);

        //reset position if it is halfway its width
        if(this.getX()<=TOTAL_WIDTH*-0.5)
            this.setX(0);
    }

}
