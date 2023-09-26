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
    private double fireChance1;
    private double fireChance2;
    private double fireChance3;
    private double fireChance4;
    private final int size;
    private Coordinate botLocation;
    private Coordinate goalLocation;
    private final HashSet<Coordinate> openCells;
    private final HashSet<Coordinate> fireCells;

    public ShipMap(int size, double q) {
        this.size = Math.abs(size);
        this.openCells = new HashSet<>();
        this.fireCells = new HashSet<>();
        this.fireChance1 = 1 - Math.pow(1 - q, 1);
        this.fireChance2 = 1 - Math.pow(1 - q, 2);
        this.fireChance3 = 1 - Math.pow(1 - q, 3);
        this.fireChance4 = 1 - Math.pow(1 - q, 4);
        generateShip();
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

    private void openCell(Coordinate cell) {
        this.openCells.add(cell);
    }

    private int countOpenNeighbors(Coordinate cell) {
        int numOpenNeighbors = 0;
        if (this.openCells.contains(cell.getAbove())) numOpenNeighbors++;
        if (this.openCells.contains(cell.getBelow())) numOpenNeighbors++;
        if (this.openCells.contains(cell.getLeft())) numOpenNeighbors++;
        if (this.openCells.contains(cell.getRight())) numOpenNeighbors++;
        return numOpenNeighbors;
    }

    private boolean isCandidate(Coordinate cell) {
        if (this.openCells.contains(cell)) return false;
        return countOpenNeighbors(cell) == 1;
    }

    private ArrayList<Coordinate> findDeadEnds() {
        ArrayList<Coordinate> result = new ArrayList<>();
        for (Coordinate cell : this.openCells) {
            if (countOpenNeighbors(cell) == 1) {
                result.add(cell);
            }
        }
        return result;
    }

    private void cullDeadEnds() {
        ArrayList<Coordinate> deadEnds = findDeadEnds();
        int numToRemove = deadEnds.size() / 2;
        while (deadEnds.size() > numToRemove) {
            int cullIndex = ThreadLocalRandom.current().nextInt(deadEnds.size());
            Coordinate cell = deadEnds.get(cullIndex);
            deadEnds.remove(cullIndex);
            Coordinate above = cell.getAbove();
            Coordinate below = cell.getBelow();
            Coordinate right = cell.getRight();
            Coordinate left = cell.getLeft();
            ArrayList<Coordinate> candidates = new ArrayList<>();
            if (above.isInBounds(this.size, this.size) && !this.openCells.contains(above))
                candidates.add(above);
            if (below.isInBounds(this.size, this.size) && !this.openCells.contains(below))
                candidates.add(below);
            if (right.isInBounds(this.size, this.size) && !this.openCells.contains(right))
                candidates.add(right);
            if (left.isInBounds(this.size, this.size) && !this.openCells.contains(left))
                candidates.add(left);
            if (candidates.size() > 0) {
                int openIndex = ThreadLocalRandom.current().nextInt(candidates.size());
                this.openCells.add(candidates.get(openIndex));
            }
        }
    }

    private HashSet<Coordinate> findCandidates() {
        HashSet<Coordinate> oneOpenNeighborCandidates = new HashSet<>();
        for (Coordinate cell : this.openCells) {
            int x = cell.toArray()[0];
            int y = cell.toArray()[1];
            if (x - 1 >= 0 && isCandidate(cell.getLeft()))
                oneOpenNeighborCandidates.add(cell.getLeft());
            if (x + 1 < this.size && isCandidate(cell.getRight()))
                oneOpenNeighborCandidates.add(cell.getRight());
            if (y - 1 >= 0 && isCandidate(cell.getBelow()))
                oneOpenNeighborCandidates.add(cell.getBelow());
            if (y + 1 < this.size && isCandidate(cell.getAbove()))
                oneOpenNeighborCandidates.add(cell.getAbove());
        }
        return oneOpenNeighborCandidates;
    }

    private void initializeOpenCells(int x, int y) {
        openCell(new Coordinate(x, y));
        boolean openedACell;
        do {
            openedACell = false;
            HashSet<Coordinate> candidates = findCandidates();
            if (candidates.size() > 0) {
                int i = 0;
                int randomIndex = ThreadLocalRandom.current().nextInt(0, candidates.size());
                Iterator<Coordinate> iterator = candidates.iterator();
                Coordinate curr;
                while (iterator.hasNext()) {
                    curr = iterator.next();
                    if (i == randomIndex) {
                        openCell(curr);
                        openedACell = true;
                        break;
                    }
                    i++;
                }
            }
        } while (openedACell);
    }

    private void generateShip() {
        int startX = ThreadLocalRandom.current().nextInt(0, this.size);
        int startY = ThreadLocalRandom.current().nextInt(0, this.size);
        initializeOpenCells(startX, startY);
        cullDeadEnds();
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
