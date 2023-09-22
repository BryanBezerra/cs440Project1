package org.introai;

import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

public class ShipMap {
    private final char OPEN_CELL = 'O';
    private final char CLOSED_CELL = 'X';
    private final int size;
    private final char[][] map;
    private final HashSet<Coordinate> openCells;

    public ShipMap(int size) {
        this.size = size;
        this.map = new char[size][size];
        this.openCells = new HashSet<>();
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                this.map[i][j] = CLOSED_CELL;
            }
        }
    }

    private void openCell(Coordinate cell) {
        int x = cell.toArray()[0];
        int y = cell.toArray()[1];
        this.map[x][y] = this.OPEN_CELL;
        this.openCells.add(cell);
    }

    private boolean isCandidate (Coordinate cell) {
        if (this.openCells.contains(cell)) return false;
        int numOpenNeighbors = 0;
        if (this.openCells.contains(cell.getAbove())) numOpenNeighbors++;
        if (this.openCells.contains(cell.getBelow())) numOpenNeighbors++;
        if (this.openCells.contains(cell.getLeft())) numOpenNeighbors++;
        if (this.openCells.contains(cell.getRight())) numOpenNeighbors++;
        return numOpenNeighbors == 1;
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

    private void generateShip() {
        int startX = ThreadLocalRandom.current().nextInt(0, this.size);
        int startY = ThreadLocalRandom.current().nextInt(0, this.size);
        openCell(new Coordinate(startX, startY));

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

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (char[] chars : this.map) {
            for (char aChar : chars) {
                result.append(aChar).append(" ");
            }
            result.append("\n");
        }
        return result.toString();
    }

    public static void main(String[] args) {
        ShipMap a = new ShipMap(50);
        a.generateShip();
        System.out.println(a);
    }
}
