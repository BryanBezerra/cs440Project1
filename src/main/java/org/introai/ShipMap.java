package org.introai;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

public class ShipMap {
    private final char OPEN_CELL = 'O';
    private final char CLOSED_CELL = '▓';
    private final int size;
    private final HashSet<Coordinate> openCells;

    public ShipMap(int size) {
        this.size = Math.abs(size);
        this.openCells = new HashSet<>();
        generateShip();
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
            int openIndex = ThreadLocalRandom.current().nextInt(candidates.size());
            this.openCells.add(candidates.get(openIndex));
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

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("  ");
        result.append("_ ".repeat(this.size));
        result.append("\n");
        for (int i = 0; i < this.size; i++) {
            result.append("| ");
            for (int j = 0; j < this.size; j++) {
                if (this.openCells.contains(new Coordinate(i, j))) {
                    result.append(OPEN_CELL).append(" ");
                } else {
                    result.append(CLOSED_CELL).append(" ");
                }
            }
            result.append("|\n");
        }
        result.append("  ");
        result.append("¯ ".repeat(this.size));
        return result.toString();
    }

    public static void main(String[] args) {
        ShipMap a = new ShipMap(20);
        System.out.println(a);
    }
}
