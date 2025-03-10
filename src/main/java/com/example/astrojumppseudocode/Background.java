package com.example.astrojumppseudocode;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;

public class Background extends SimpleMovingImage {
    //general info
    private final int TOTAL_WIDTH;
    private final int INITIAL_PLANET_INDEX;

    Background(ImageView img,int width, int height,float speedX,int totalWidth,int planetIndex){
        super(img,width,height,speedX,0);
        this.TOTAL_WIDTH = totalWidth;
        this.INITIAL_PLANET_INDEX = planetIndex;

        //set viewport
        changePlanet(INITIAL_PLANET_INDEX);
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
    public void changePlanet(int planetIndex){
        //set the viewpoint to a rectangle situated at the planet's index
        this.getImage().setViewport(new Rectangle2D(this.getWidth(),this.getHeight()*planetIndex,this.getWidth(),this.getHeight()));
    }


}
