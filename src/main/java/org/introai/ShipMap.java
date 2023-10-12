package org.introai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

public class ShipMap {
    private final int size;
    private Coordinate botLocation;
    private Coordinate goalLocation;
    private final HashSet<Coordinate> openCells;
    private final HashSet<Coordinate> fireCells;

    public ShipMap(int size) {
        this.size = Math.abs(size);
        this.openCells = new ShipCreator(size).generateShip();
        this.fireCells = new HashSet<>();
        placeBotGoalAndFire();
    }

    /**
     * Initial placement of bot, goal, and fire.
     */
    private void placeBotGoalAndFire() {
        int sizeOpenCells = this.openCells.size();
        int goalI;
        int botI;
        int fireI;

        goalI = ThreadLocalRandom.current().nextInt(sizeOpenCells);
        do {
            botI = ThreadLocalRandom.current().nextInt(sizeOpenCells);
        } while (botI == goalI);
        do {
            fireI = ThreadLocalRandom.current().nextInt(sizeOpenCells);
        } while (fireI == botI || fireI == goalI);

//        System.out.println(goalI + " " + botI + " " + fireI);

        int i = 0;
        Coordinate cellToBurn = null;
        for (Coordinate cell : this.openCells) {
            if (i == goalI) this.goalLocation = cell;
            else if (i == botI) this.botLocation = cell;
            else if (i == fireI) cellToBurn = cell;
            i++;
        }
        igniteCell(cellToBurn);
    }

    /**
     * Sets a previously open cell on fire.
     *
     * @param cell the cell to be ignited
     */
    public void igniteCell(Coordinate cell) {
        if (openCells.contains(cell)) {
            fireCells.add(cell);
            openCells.remove(cell);
        }
    }

    /**
     * Finds a given cell's open neighbors. If there are less than 4 open cells,
     * the array contains null values at the end to pad the length.
     *
     * @param cell the cell to be searched from
     * @return an array of length 4. The first n indices contain the cell's n open neighbors.
     */
    public Coordinate[] openNeighbors(Coordinate cell) {
        final int POSSIBLE_NEIGHBORS = 4;
        Coordinate[] result = new Coordinate[POSSIBLE_NEIGHBORS];
        Coordinate[] neighbors = {cell.getAbove(), cell.getBelow(), cell.getLeft(), cell.getRight()};
        int currIndex = 0;
        for (Coordinate curr : neighbors) {
            if (openCells.contains(curr)) {
                result[currIndex] = curr;
                currIndex++;
            }
        }
        return result;
    }

//    public HashSet<Coordinate> getOpenCells() {
//        return (HashSet<Coordinate>) openCells.clone();
//    }
//
//    public HashSet<Coordinate> getFireCells() {
//        return (HashSet<Coordinate>) fireCells.clone();
//    }

    public Coordinate getGoalLocation() {
        return goalLocation.copy();
    }

    public Coordinate getBotLocation() {
        return botLocation.copy();
    }

    public void setGoalLocation(Coordinate newGoalLocation) {
        this.goalLocation = newGoalLocation;
    }

    public void setBotLocation(Coordinate newBotLocation) {
        this.botLocation = newBotLocation;
    }

    @Override
    public String toString() {
        final char OPEN_CELL = 'O';
        final char CLOSED_CELL = '▓';
        final char ON_FIRE = 'F';
        final char BOT = 'B';
        final char GOAL = 'G';
        StringBuilder result = new StringBuilder();

        result.append("  ");
        result.append("_ ".repeat(this.size));
        result.append("\n");
        for (int i = 0; i < this.size; i++) {
            result.append("| ");
            for (int j = 0; j < this.size; j++) {
                Coordinate curr = new Coordinate(i, j);
                if (curr.equals(this.botLocation)) {
                    result.append(BOT);
                } else if (curr.equals(this.goalLocation)) {
                    result.append(GOAL);
                } else if (this.fireCells.contains(curr)) {
                    result.append(ON_FIRE);
                } else if (this.openCells.contains(curr)) {
                        result.append(OPEN_CELL);
                } else {
                    result.append(CLOSED_CELL);
                }
                result.append(" ");
            }
            result.append("|\n");
        }
        result.append("  ");
        result.append("¯ ".repeat(this.size));
        return result.toString();
    }

    public static void main(String[] args) {
        ShipMap a = new ShipMap(15);
        System.out.println(a);
        System.out.println("Bot: " + a.botLocation);
        System.out.println("Goal: " + a.goalLocation);
        System.out.println("Initial fire: " + a.fireCells);
    }
}
