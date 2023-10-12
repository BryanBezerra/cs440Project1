package org.introai;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

public class ShipMap {
    private final char OPEN_CELL = 'O';
    private final char CLOSED_CELL = '▓';
    private final char ON_FIRE = 'F';
    private final char BOT = 'B';
    private final char GOAL = 'G';

    private final double fireChance1;
    private final double fireChance2;
    private final double fireChance3;
    private final double fireChance4;

    private final int size;
    private Coordinate botLocation;
    private Coordinate goalLocation;
    private final HashSet<Coordinate> openCells;
    private final HashSet<Coordinate> fireCells;

    public ShipMap(int size, double q) {
        this.size = Math.abs(size);
        this.openCells = new ShipCreator(size).generateShip();
        this.fireCells = new HashSet<>();
        this.fireChance1 = 1 - Math.pow(1 - q, 1);
        this.fireChance2 = 1 - Math.pow(1 - q, 2);
        this.fireChance3 = 1 - Math.pow(1 - q, 3);
        this.fireChance4 = 1 - Math.pow(1 - q, 4);
        placeBotGoalAndFire();
    }

    private void placeBotGoalAndFire() {
        int sizeOpenCells = this.openCells.size();
        int goalI = ThreadLocalRandom.current().nextInt(sizeOpenCells);
        int botI;
        int fireI;
        do {
            botI = ThreadLocalRandom.current().nextInt(sizeOpenCells);
        } while (botI == goalI);
        do {
            fireI = ThreadLocalRandom.current().nextInt(sizeOpenCells);
        } while (fireI == botI || fireI == goalI);
        int i = 0;
        for (Coordinate cell : this.openCells) {
            if (i == goalI) this.goalLocation = cell;
            i++;
        }
        i = 0;
        for (Coordinate cell : this.openCells) {
            if (i == botI) this.botLocation = cell;
            i++;
        }
        Coordinate cellToRemove = null;
        i = 0;
        for (Coordinate cell : this.openCells) {
            if (i == fireI) {
                this.fireCells.add(cell);
                cellToRemove = cell;
            }
            i++;
        }
        this.openCells.remove(cellToRemove);
    }

//    private boolean isCatchingFire(int neighborsOnFire) {
//        //TODO Write logic for setting cells on fire
//        switch (neighborsOnFire)
//
//        return true;
//    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("  ");
        result.append("_ ".repeat(this.size));
        result.append("\n");
        for (int i = 0; i < this.size; i++) {
            result.append("| ");
            for (int j = 0; j < this.size; j++) {
                Coordinate curr = new Coordinate(i, j);
                if (curr.equals(this.botLocation)) {
                    result.append(this.BOT);
                } else if (curr.equals(this.goalLocation)) {
                    result.append(this.GOAL);
                } else if (this.fireCells.contains(curr)) {
                    result.append(this.ON_FIRE);
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
        ShipMap a = new ShipMap(15, 0.5);
        System.out.println(a);
    }
}
