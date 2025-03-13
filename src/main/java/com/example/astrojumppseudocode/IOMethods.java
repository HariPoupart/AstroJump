package com.example.astrojumppseudocode;

import java.io.*;

public class IOMethods {

    //old/player attributes
    private  static int playerHighScore;
    private static int playerTotalStarsCollected;
    private static StringBuilder playerPlanetsDiscovered;

    //current attributes
    private static int currentHighScore = 0;
    private static int currentStarsCollected = 0;
    //bit string of 8 digits, nth bit is 0 if player has yet to encounter it, turned into a 1 when player encounters
    private static StringBuilder planetsDiscoveredBitString;


    //constructor
    IOMethods(int currentHighScore, int newStarsCollected, StringBuilder planetsDiscoveredBitString) {
        //set attributes
        this.currentHighScore = currentHighScore;
        this.currentStarsCollected = newStarsCollected;
        this.planetsDiscoveredBitString = planetsDiscoveredBitString;
        //updating player's attribute
        playerHighScore = getHighScore();
        playerTotalStarsCollected = getTotalStarsCollected();
        playerPlanetsDiscovered = getPlanetsDiscovered();
        //assuring well parsed
        System.out.println(currentHighScore + " " + newStarsCollected + " " + planetsDiscoveredBitString);
        //setting currentHighScore
        setHighScore(currentHighScore);

        //setting currentTotalStarsCollected
        setTotalStarsCollected();

        //setting planetsDiscoveredBitString
        setPlanetsDiscoveredBitString();
    }

    //accessor methods
    public static int getHighScore() {
        File fileHS = new File("highScore.txt");
        try {
            FileInputStream fileStreamHS = new FileInputStream(fileHS);
            DataInputStream inputHS = new DataInputStream(fileStreamHS);
            playerHighScore = inputHS.readInt();
        }
        catch(FileNotFoundException e) {
            System.out.println("highScore.txt file not found IOMethods constructor");
        } catch (IOException e) {
//            throw new RuntimeException(e);
        }
        return playerHighScore;
    }
    public static int getTotalStarsCollected() {
        File fileTS = new File("totalNumberStar.txt");
        try {
            FileInputStream fileStreamTS = new FileInputStream(fileTS);
            DataInputStream inputTS = new DataInputStream(fileStreamTS);
            playerTotalStarsCollected = inputTS.readInt();
        }
        catch(FileNotFoundException e) {
            System.out.println("totalNumberStar.txt file not found IOMethods constructor");
        } catch (IOException e) {
        }
        return playerTotalStarsCollected;
    }
    public static StringBuilder getPlanetsDiscovered() {
        File filePD = new File("planetsDiscovered.txt");
        try {
            FileInputStream fileStreamPD = new FileInputStream(filePD);
//            DataInputStream inputPD = new DataInputStream(fileStreamPD);
            BufferedInputStream inputt = new BufferedInputStream(fileStreamPD);
            playerPlanetsDiscovered = new StringBuilder(new String(inputt.readAllBytes()));
            System.out.println(playerPlanetsDiscovered);
//            playerPlanetsDiscovered = new StringBuilder(inputPD.readInt() + "");
        } catch (FileNotFoundException e) {
            System.out.println("planetsDiscovered.txt file not found IOMethods constructor");
        } catch (IOException e) {
//            throw new RuntimeException(e);
        }
        return playerPlanetsDiscovered;
    }

    //mutator methods
    public static void setHighScore(int newHighScore) {
        if(currentHighScore > playerHighScore) {
            File file = new File("highScore.txt");
            try {
                FileOutputStream fileStream = new FileOutputStream(file);
                DataOutputStream output = new DataOutputStream(fileStream) {
                };
                output.write(newHighScore);
            } catch (FileNotFoundException e) {
                System.out.println("highScore.txt file not found IOMethods setHighScore");
            } catch (IOException e) {
//                throw new RuntimeException(e);
            }
        }
    }
    public static void setTotalStarsCollected() {
        File file = new File("totalNumberStar.txt");
        try {
            FileOutputStream fileStream = new FileOutputStream(file);
            DataOutputStream output = new DataOutputStream(fileStream) {
            };
            output.write(currentStarsCollected + playerTotalStarsCollected);
        } catch (FileNotFoundException e) {
            System.out.println("totalNumberStar.txt file not found IOMethods setTotalStarsCollected");
        } catch (IOException e) {
//            throw new RuntimeException(e);
        }
    }
    public static void setPlanetsDiscoveredBitString() {
        //merge both bit strings current and total
        StringBuilder totalPlanets = getPlanetsDiscovered();
        StringBuilder currentPlanets = planetsDiscoveredBitString;
        StringBuilder mergedPlanets = new StringBuilder("");
        for(int i = 0; i < totalPlanets.length(); i++) {
            if(totalPlanets.charAt(i) >= currentPlanets.charAt(i)) {
                mergedPlanets.setCharAt(i, totalPlanets.charAt(i));
            }
            else {
                mergedPlanets.setCharAt(i, currentPlanets.charAt(i));
            }
        }
        File file = new File("planetsDiscovered.txt");
        try {
            FileOutputStream fileStream = new FileOutputStream(file);
            DataOutputStream output = new DataOutputStream(fileStream) {
            };
            output.write(Integer.parseInt(mergedPlanets.toString()));
        }
        catch(FileNotFoundException e) {
            System.out.println("planetsDiscovered.txt file not found IOMethods setPlanetsDiscoveredBitString");
        } catch (IOException e) {
//            throw new RuntimeException(e);
        }
    }
    }
