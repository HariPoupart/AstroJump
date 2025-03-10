package com.example.astrojumppseudocode;

import java.io.*;

public class IOMethods {

    //attributes
    public int currentHighScore = 0;
    public int currentTotalStarsCollected;

    //constructors
    IOMethods() {

        //setting currentHighScore
        File fileHS = new File("highScore.txt");
        try {
            FileInputStream fileStreamHS = new FileInputStream(fileHS);
            DataInputStream inputHS = new DataInputStream(fileStreamHS);
            currentHighScore = inputHS.readInt();
        }
        catch(FileNotFoundException e) {
            System.out.println("highScore.txt file not found IOMethods setHighScore");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //setting currentTotalStarsCollected
        File fileTS = new File("highScore.txt");
        try {
            FileInputStream fileStreamTS = new FileInputStream(fileTS);
            DataInputStream inputTS = new DataInputStream(fileStreamTS);
            currentTotalStarsCollected = inputTS.readInt();
        }
        catch(FileNotFoundException e) {
            System.out.println("highScore.txt file not found IOMethods setHighScore");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    //accessor and mutator methods
    public int getHighScore() {
        return currentHighScore;
    }
    public int getTotalStarsCollected() {
        return currentTotalStarsCollected;
    }

    public void setHighScore(int newHighScore) {
        if(newHighScore > currentHighScore) {
            File file = new File("highScore.txt");
            try {
                FileOutputStream fileStream = new FileOutputStream(file);
                DataOutputStream output = new DataOutputStream(fileStream) {
                };
                output.write(newHighScore);
            } catch (FileNotFoundException e) {
                System.out.println("highScore.txt file not found IOMethods setHighScore");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void setTotalStarsCollected(int newStarsCollected) {
        File file = new File("totalNumberStar.txt");
        try {
            FileOutputStream fileStream = new FileOutputStream(file);
            DataOutputStream output = new DataOutputStream(fileStream) {
            };
            output.write(newStarsCollected + currentTotalStarsCollected);
        }
        catch(FileNotFoundException e) {
            System.out.println("totalNumberStar.txt file not found IOMethods setTotalStarsCollected");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
