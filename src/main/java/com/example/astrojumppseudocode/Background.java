package com.example.astrojumppseudocode;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;

public class Background extends SimpleMovingImage {
    //general info
    private final int WIDTH;
    private final int HEIGHT;
    private final int INITIAL_PLANET_INDEX;

    Background(ImageView img,int fitWidth,int fitHeight,int width, int height,float speedX,int planetIndex){
        super(img,fitWidth,fitHeight,speedX,0);
        this.WIDTH = width;
        this.HEIGHT = height;
        this.INITIAL_PLANET_INDEX = planetIndex;

        //set viewport
        changePlanet(INITIAL_PLANET_INDEX);

        //set coordinates
        this.setX(0);
        this.setY(0);
    }

    @Override
    public void updatePosition(double deltaTime){
        //update x
        setX(getX()+this.getSpeedX()*deltaTime);
        //update y
        setY(getY()+this.getSpeedY()*deltaTime);

        //reset position if it is halfway its width
        if(this.getX()<=this.getWidth()*-0.5)
            this.setX(0);
    }
    public void changePlanet(int planetIndex){
        //set the viewpoint to a rectangle situated at the planet's index
        this.getImage().setViewport(new Rectangle2D(0,HEIGHT*planetIndex,WIDTH,HEIGHT));
    }

}
