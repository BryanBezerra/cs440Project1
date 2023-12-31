package org.introai;

import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

public class ShipMap {
    private final int size;
    private double flammability;
    private Coordinate goalLocation;
    private Coordinate botLocation;
    private final HashSet<Coordinate> openCells;
    private final HashSet<Coordinate> fireCells;

    /**
     * Creates a new ship.
     *
     * @param size the height and width of the ship
     * @param flammability how fast the fire spreads on the ship (0.0 - 1.0)
     */
    public ShipMap(int size, double flammability) {
        this.size = Math.abs(size);
        this.flammability = flammability;
        this.openCells = new ShipCreator(size).generateShip();
        this.fireCells = new HashSet<>();
        int sizeOpenCells = this.openCells.size();
        int goalI;
        int botI;
        int fireI;

        // Randomly place the bot, the goal, and set one cell on fire
        goalI = ThreadLocalRandom.current().nextInt(sizeOpenCells);
        do {
            botI = ThreadLocalRandom.current().nextInt(sizeOpenCells);
        } while (botI == goalI);
        do {
            fireI = ThreadLocalRandom.current().nextInt(sizeOpenCells);
        } while (fireI == botI || fireI == goalI);

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
     * Creates a ship with preset values. Used for making copies and testing.
     *
     * @param size the size of the ship
     * @param flammability how fast the fire spreads
     * @param goalLocation the location of the goal
     * @param botLocation the location of the bot
     * @param openCells the set of open cells on the ship
     * @param fireCells the set of ignited cells
     */
    private ShipMap(int size, double flammability, Coordinate goalLocation, Coordinate botLocation,
                    HashSet<Coordinate> openCells, HashSet<Coordinate> fireCells) {
        this.size = size;
        this.flammability = flammability;
        this.goalLocation = goalLocation;
        this.botLocation = botLocation;
        this.openCells = openCells;
        this.fireCells = fireCells;
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
     * Finds a given cell's open neighbors.
     *
     * @param cell the cell to be searched from
     * @return an array of Coordinates containing the cell's open neighbors
     */
    public Coordinate[] openNeighbors(Coordinate cell) {
        final int POSSIBLE_NEIGHBORS = 4;
        Coordinate[] openNeighbors = new Coordinate[POSSIBLE_NEIGHBORS];
        Coordinate[] neighbors = {cell.getAbove(), cell.getBelow(), cell.getLeft(), cell.getRight()};
        int currIndex = 0;
        for (Coordinate neighbor : neighbors) {
            if (openCells.contains(neighbor)) {
                openNeighbors[currIndex] = neighbor;
                currIndex++;
            }
        }

        Coordinate[] result = new Coordinate[currIndex];
        currIndex = 0;
        for (Coordinate neighbor : openNeighbors) {
            if (neighbor == null) break;
            result[currIndex] = neighbor;
            currIndex++;
        }
        return result;
    }

    /**
     * Moves the bot up one cell.
     *
     * @throws RuntimeException if bot tries to move to an invalid cell.
     */
    public void moveBotUp() throws RuntimeException {
        Coordinate above = botLocation.getAbove();
        if (openCells.contains(above) || fireCells.contains(above)) this.botLocation = above;
        else throw new RuntimeException("Bot tried to move to an invalid cell");
    }

    /**
     * Moves the bot down one cell.
     *
     * @throws RuntimeException if bot tries to move to an invalid cell.
     */
    public void moveBotDown() throws RuntimeException {
        Coordinate below = botLocation.getBelow();
        if (openCells.contains(below) || fireCells.contains(below)) this.botLocation = below;
        else throw new RuntimeException("Bot tried to move to an invalid cell");
    }

    /**
     * Moves the bot left one cell.
     *
     * @throws RuntimeException if bot tries to move to an invalid cell.
     */
    public void moveBotLeft() throws RuntimeException {
        Coordinate left = botLocation.getLeft();
        if (openCells.contains(left) || fireCells.contains(left)) this.botLocation = left;
        else throw new RuntimeException("Bot tried to move to an invalid cell");
    }

    /**
     * Moves the bot right one cell.
     *
     * @throws RuntimeException if bot tries to move to an invalid cell.
     */
    public void moveBotRight() throws RuntimeException {
        Coordinate right = botLocation.getRight();
        if (openCells.contains(right) || fireCells.contains(right)) this.botLocation = right;
        else throw new RuntimeException("Bot tried to move to an invalid cell");
    }

    /**
     * The open cells on the ship.
     *
     * @return a copy of the set of open cells
     */
    public HashSet<Coordinate> getOpenCells() {
        return (HashSet<Coordinate>) openCells.clone();
    }

    /**
     * The ignited cells on the ship.
     *
     * @return a copy of the ignited cells on the ship
     */
    public HashSet<Coordinate> getFireCells() {
        return (HashSet<Coordinate>) fireCells.clone();
    }

    /**
     * Detects whether the given cell is on fire.
     *
     * @param cell the cell in question
     * @return true if the cell is on fire, otherwise false
     */
    public boolean isOnFire(Coordinate cell) {
        return fireCells.contains(cell);
    }

    /**
     * The Coordinate of the goal on the ship.
     * @return the Coordinate of the goal on the ship
     */
    public Coordinate getGoalLocation() {
        return goalLocation.copy();
    }

    /**
     * The location of the bot.
     * @return the location of the bot
     */
    public Coordinate getBotLocation() {
        return botLocation.copy();
    }

    /**
     * The ship's flammability (q).
     * @returnhe the ship's flammability (q)
     */
    public double getFlammability() {
        return flammability;
    }

    /**
     * Creates a new ship with the same state as the current ship.
     *
     * @return a new ship with the same state as the current ship
     */
    public ShipMap copyState() {
        HashSet<Coordinate> newOpenCells = getOpenCells();
        HashSet<Coordinate> newFireCells = getFireCells();

        return new ShipMap(size, flammability, goalLocation.copy(),
                botLocation.copy(), newOpenCells, newFireCells);
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
}
