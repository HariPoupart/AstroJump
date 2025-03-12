package com.example.astrojumppseudocode;

import java.io.*;

public class IOMethods {

    //attributes
    private static int currentHighScore = 0;
    private static int currentTotalStarsCollected = 0;
    //bit string of 8 digits, nth bit is 0 if player has yet to encounter it, turned into a 1 when player encounters
    private static int planetsDiscoveredBitString;


    //constructors
    IOMethods(int currentHighScore, int newStarsCollected, int planetsDiscoveredBitString) {
        //assuring well parsed
        System.out.println(currentHighScore + " " + newStarsCollected + " " + planetsDiscoveredBitString);
        //setting currentHighScore
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

        }
        //setting currentTotalStarsCollected
        File fileTS = new File("totalNumberStar.txt");
        try {
            FileInputStream fileStreamTS = new FileInputStream(fileTS);
            DataInputStream inputTS = new DataInputStream(fileStreamTS);
            currentTotalStarsCollected = inputTS.readInt();
        }
        catch(FileNotFoundException e) {
            System.out.println("totalNumberStar.txt file not found IOMethods constructor");
        } catch (IOException e) {

        }
        //setting planetsDiscoveredBitString

    }

    //accessor methods
    public static int getHighScore() {
        File fileHS = new File("highScore.txt");
        try {
            FileInputStream fileStreamHS = new FileInputStream(fileHS);
            DataInputStream inputHS = new DataInputStream(fileStreamHS);
            currentHighScore = inputHS.readInt();
        }
        catch(FileNotFoundException e) {
            System.out.println("highScore.txt file not found IOMethods constructor");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return currentHighScore;
    }
    public static int getTotalStarsCollected() {
        File fileTS = new File("totalNumberStar.txt");
        try {
            FileInputStream fileStreamTS = new FileInputStream(fileTS);
            DataInputStream inputTS = new DataInputStream(fileStreamTS);
            currentTotalStarsCollected = inputTS.readInt();
        }
        catch(FileNotFoundException e) {
            System.out.println("totalNumberStar.txt file not found IOMethods constructor");
        } catch (IOException e) {
        }
        return currentTotalStarsCollected;
    }
    public static String getPlanetsDiscovered() {
        File filePD = new File("planetsDiscovered.txt");
        try {
            FileInputStream fileStreamPD = new FileInputStream(filePD);
            DataInputStream inputPD = new DataInputStream(fileStreamPD);
            planetsDiscoveredBitString = (inputPD.readInt());
        } catch (FileNotFoundException e) {
            System.out.println("planetsDiscovered.txt file not found IOMethods constructor");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return planetsDiscoveredBitString + "";
    }

    //mutator methods
    public static void setHighScore(int newHighScore) {
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
    public static void setTotalStarsCollected(int newStarsCollected) {
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
    public static void setPlanetsDiscoveredBitString(int planetsDiscoveredBitString) {
        //merge both bit strings current and total
        String totalPlanets = getPlanetsDiscovered();
        String currentPlanets = planetsDiscoveredBitString + "";
        String mergedPlanets = "";
        for(int i = 0; i < totalPlanets.length(); i++) {
            if(totalPlanets.charAt(i) >= currentPlanets.charAt(i)) {
                mergedPlanets += totalPlanets.charAt(i);
            }
            else {
                mergedPlanets += currentPlanets.charAt(i);
            }
        }
        File file = new File("planetsDiscovered.txt");
        try {
            FileOutputStream fileStream = new FileOutputStream(file);
            DataOutputStream output = new DataOutputStream(fileStream) {
            };
            output.write(Integer.parseInt(mergedPlanets));
        }
        catch(FileNotFoundException e) {
            System.out.println("planetsDiscovered.txt file not found IOMethods setPlanetsDiscoveredBitString");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
