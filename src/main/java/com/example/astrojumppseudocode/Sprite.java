package com.example.astrojumppseudocode;


import javafx.scene.image.ImageView;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;

import javafx.util.Duration;

public class Sprite extends Transition {

    private final ImageView IMAGE_VIEW;
    private int row;
    private final int NO_OF_COLUMNS;
    private final int OFFSET_x;
    private final int OFFSET_Y;
    private final int WIDTH;
    private final int HEIGHT;

    private int lastIndex;

    public Sprite(ImageView imageView, Duration duration, int row, int columns, int offsetX, int offsetY, int width, int height) {
        this.IMAGE_VIEW = imageView;
        this.row = row;
        this.NO_OF_COLUMNS = columns;
        this.OFFSET_x = offsetX;
        this.OFFSET_Y = offsetY;
        this.WIDTH = width;
        this.HEIGHT = height;
        setCycleDuration(duration);
        setInterpolator(Interpolator.LINEAR);
    }

    public void interpolate(double k) {
        //k is a fraction from 0.0 to 1.0 that shows the progress of the transition
        //sets the index to k*number of columns (don't go past the last index)
        final int index = Math.min((int) Math.floor(k * NO_OF_COLUMNS),NO_OF_COLUMNS-1);
        if (index != lastIndex) {
            //set coordinates of the image to show
            final int x = index  * WIDTH + OFFSET_x;
            final int y = row * HEIGHT + OFFSET_Y;
            //show only the image
            IMAGE_VIEW.setViewport(new Rectangle2D(x, y, WIDTH, HEIGHT));
            lastIndex = index;
        }
    }

    public void setRow(int row){
        this.row = row;
    }
    public int getRow(){
        return this.row;
    }
}
