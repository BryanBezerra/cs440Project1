package org.introai;

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

    /**
     * Decides whether a cell is to catch fire during the current tick.
     *
     * @param neighborsOnFire number of surrounding cells on fire before current tick
     * @return true if cell catches fire, otherwise false
     * @throws IllegalArgumentException if 0 <= neighborsOnFire <= 4
     */
    private boolean willCatchFire(int neighborsOnFire) throws IllegalArgumentException {
        if (neighborsOnFire == 0) return false;
        double probabilityOfFire;
        switch (neighborsOnFire) {
            case 1 -> probabilityOfFire = this.fireChance1;
            case 2 -> probabilityOfFire = this.fireChance2;
            case 3 -> probabilityOfFire = this.fireChance3;
            case 4 -> probabilityOfFire = this.fireChance4;
            default -> throw new IllegalArgumentException("Neighbors on fire must be 1, 2, 3, or 4. " +
                    neighborsOnFire + " is not valid.");
        }
        double roll = ThreadLocalRandom.current().nextDouble();
        return roll < probabilityOfFire;
    }


}
