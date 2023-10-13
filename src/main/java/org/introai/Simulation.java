package org.introai;

import org.introai.bots.Bot;
import org.introai.bots.Bot1;
import org.introai.bots.Bot2;
import org.introai.bots.Bot3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

public class Simulation {
    private final ShipMap shipMap;
    private final HashSet<Coordinate> catchingFire;

    private final double fireChance1;
    private final double fireChance2;
    private final double fireChance3;
    private final double fireChance4;

    public Simulation(int shipSize, double shipFlammability) {
        this.shipMap = new ShipMap(shipSize);
        this.catchingFire = new HashSet<>();
        this.fireChance1 = 1 - Math.pow(1 - shipFlammability, 1);
        this.fireChance2 = 1 - Math.pow(1 - shipFlammability, 2);
        this.fireChance3 = 1 - Math.pow(1 - shipFlammability, 3);
        this.fireChance4 = 1 - Math.pow(1 - shipFlammability, 4);
    }

    private void simulateFireSpread() {
        HashSet<Coordinate> openCells = shipMap.getOpenCells();
        for (Coordinate cell : openCells) {
            if (willCatchFire(cell)) catchingFire.add(cell);
        }

        for (Coordinate cell : catchingFire) {
            shipMap.igniteCell(cell);
        }
        catchingFire.clear();
    }

    /**
     * Decides whether a cell will catch fire during the current tick.
     *
     * @param cell the cell that might catch fire
     * @return true if cell catches fire, otherwise false
     * @throws RuntimeException if 0 <= neighborsOnFire <= 4
     */
    private boolean willCatchFire(Coordinate cell) throws RuntimeException {
        int neighborsOnFire = 0;
        Coordinate[] neighbors = {cell.getAbove(), cell.getBelow(), cell.getLeft(), cell.getRight()};

        for (Coordinate neighbor : neighbors) {
            if (shipMap.isOnFire(neighbor)) neighborsOnFire++;
        }
        if (neighborsOnFire == 0) return false;

        double probabilityOfFire;
        switch (neighborsOnFire) {
            case 1 -> probabilityOfFire = this.fireChance1;
            case 2 -> probabilityOfFire = this.fireChance2;
            case 3 -> probabilityOfFire = this.fireChance3;
            case 4 -> probabilityOfFire = this.fireChance4;
            default -> throw new RuntimeException("Neighbors on fire must be 0, 1, 2, 3, or 4. " +
                    neighborsOnFire + " is not valid.");
        }

        double roll = ThreadLocalRandom.current().nextDouble();
        return roll < probabilityOfFire;
    }

    /**
     * Runs one simulation until either the bot reaches its goal or the goal becomes unreachable.
     *
     * @param bot the bot that will make decisions
     * @return true if the bot was successful, otherwise false
     */
    public boolean run (Bot bot) {
        Coordinate goal = shipMap.getGoalLocation();
        boolean botCanMove = true;
        boolean goalAchieved = false;

        while (botCanMove && !goalAchieved) {
            botCanMove = bot.makeAMove();
            Coordinate botLocation = shipMap.getBotLocation();
            if (botLocation.equals(goal)) goalAchieved = true;
            simulateFireSpread();
            if (shipMap.isOnFire(botLocation) || shipMap.isOnFire(goal)) botCanMove = false;
        }
        return goalAchieved;
    }

    public ShipMap getShipMap() {
        return shipMap;
    }

    public static void main(String[] args) {
        Simulation test = new Simulation(10, 0.45);
        System.out.println(test.shipMap);
        boolean result = test.run(new Bot3(test.getShipMap()));
        System.out.println(result);
        System.out.println(test.shipMap);
    }
}
