package com.example.astrojumppseudocode;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class IOMethods {

    //old/player written attributes
    private static long playerHighScore;
    private static int playerTotalStarsCollected;
    private static StringBuilder playerPlanetsDiscovered;

    //current attributes
    private static long currentHighScore = 0;
    private static int currentStarsCollected = 0;
    //bit string of 8 digits, nth bit is 0 if player has yet to encounter it, turned into a 1 when player encounters
    private static StringBuilder planetsDiscoveredBitString;

    //attributes for settings
    private static String jumpControl = "SPACE";
    private static String netThrow = "";


    //constructor
    IOMethods(long currentHighScore, int newStarsCollected, StringBuilder planetsDiscoveredBitString) {
        //set attributes
        this.currentHighScore = currentHighScore;
        this.currentStarsCollected = newStarsCollected;
        this.planetsDiscoveredBitString = planetsDiscoveredBitString;
        //updating player's attribute
        playerHighScore = getHighScore();
        playerTotalStarsCollected = getTotalStarsCollected();
        playerPlanetsDiscovered = getPlanetsDiscovered();
        //setting currentHighScore
        setHighScore();

        //setting currentTotalStarsCollected
        setTotalStarsCollected();

        //setting planetsDiscoveredBitString
        setPlanetsDiscoveredBitString();

    }

    //accessor methods
    public static long getHighScore() {
        File fileHS = new File("highScore.txt");
        if(!fileHS.exists()) {
            try{
            PrintWriter create = new PrintWriter(fileHS);
            create.write("");
            create.close();
            } catch(Exception e) {

            }
        }
        try {
            FileInputStream fileStreamHS = new FileInputStream(fileHS);
            DataInputStream inputHS = new DataInputStream(fileStreamHS);
            if (inputHS.available() != 0) {
                playerHighScore = inputHS.readInt();
            } else {
                playerHighScore = 0;
            }
            inputHS.close();
        } catch (FileNotFoundException e) {
            System.out.println("highScore.txt file not found IOMethods constructor");
        } catch (IOException e) {
            System.out.print("error getHighScore");
        }
        return playerHighScore;
    }

    public static int getTotalStarsCollected() {
        File fileTS = new File("totalNumberStar.txt");
        if(!fileTS.exists()) {
            try{
                PrintWriter create = new PrintWriter(fileTS);
                create.write("");
                create.close();
            } catch(Exception e) {

            }
        }
        try {
            FileInputStream fileStreamTS = new FileInputStream(fileTS);
            DataInputStream inputTS = new DataInputStream(fileStreamTS);
            if (inputTS.available() != 0) {
                playerTotalStarsCollected = inputTS.readInt();
            } else {
                playerTotalStarsCollected = 0;
            }
            inputTS.close();
        } catch (FileNotFoundException e) {
            System.out.println("totalNumberStar.txt file not found IOMethods constructor");
        } catch (IOException e) {
        }
        return playerTotalStarsCollected;
    }

    public static StringBuilder getPlanetsDiscovered() {
        File filePD = new File("planetsDiscovered.txt");
        if(!filePD.exists()) {
            try{
                PrintWriter create = new PrintWriter(filePD);
                create.write("");
                create.close();
            } catch(Exception e) {

            }
        }
        if (filePD.exists()) {
            try {
                FileInputStream fileStreamPD = new FileInputStream(filePD);
                BufferedInputStream input = new BufferedInputStream(fileStreamPD);
                if (input.available() != 0) {
                    String planetsByteToString = new String(input.readAllBytes(), StandardCharsets.UTF_8);
                    playerPlanetsDiscovered = new StringBuilder(planetsByteToString);
                } else {
                    playerPlanetsDiscovered = new StringBuilder("00000000");
                }
                input.close();
            } catch (FileNotFoundException e) {
                System.out.println("planetsDiscovered.txt file not found IOMethods constructor");
            } catch (IOException e) {
                System.out.print("error getPlanetDiscovered");
            }
        }
        return playerPlanetsDiscovered;
    }

    //mutator methods
    public static void setHighScore() {
        if (currentHighScore > playerHighScore) {
            File file = new File("highScore.txt");
            try {
                FileOutputStream fileStream = new FileOutputStream(file);
                DataOutputStream output = new DataOutputStream(fileStream);
                output.writeInt((int) currentHighScore);
                output.close();
            } catch (FileNotFoundException e) {
                System.out.println("highScore.txt file not found IOMethods setHighScore");
            } catch (IOException e) {
                System.out.print("error setHighScore");
            }
        }
    }

    public static void setTotalStarsCollected() {
        File file = new File("totalNumberStar.txt");
        try {
            FileOutputStream fileStream = new FileOutputStream(file);
            DataOutputStream output = new DataOutputStream(fileStream);
            output.writeInt((playerTotalStarsCollected + currentStarsCollected));
            output.close();
        } catch (FileNotFoundException e) {
            System.out.println("totalNumberStar.txt file not found IOMethods setTotalStarsCollected");
        } catch (IOException e) {

            System.out.print("error setStars");
        }
    }

    public static void setPlanetsDiscoveredBitString() {
        //merge both bit strings current and total
        StringBuilder totalPlanets = getPlanetsDiscovered();
        StringBuilder currentPlanets = planetsDiscoveredBitString;
        StringBuilder mergedPlanets = new StringBuilder("00000000");
        for (int i = 0; i < totalPlanets.length(); i++) {
            if (totalPlanets.charAt(i) >= currentPlanets.charAt(i)) {
                mergedPlanets.setCharAt(i, totalPlanets.charAt(i));
            } else {
                mergedPlanets.setCharAt(i, currentPlanets.charAt(i));
            }
        }
        //write new mergedPlanets into file
        File file = new File("planetsDiscovered.txt");
        try {
            FileOutputStream fileStream = new FileOutputStream(file);
            BufferedOutputStream output = new BufferedOutputStream(fileStream);
            output.write(mergedPlanets.toString().getBytes());
            output.close();
        } catch (FileNotFoundException e) {
            System.out.println("planetsDiscovered.txt file not found IOMethods setPlanetsDiscoveredBitString");
        } catch (IOException e) {
            System.out.print("error setPlanet");
        }
    }

    public static void reset() {
        File fileHS = new File("highScore.txt");
        fileHS.delete();
        File fileNS = new File("totalNumberStar.txt");
        fileNS.delete();
        File filePD =new File("planetsDiscovered.txt");
        filePD.delete();

    }
}
