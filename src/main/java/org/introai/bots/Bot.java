package org.introai.bots;

import org.introai.ShipMap;

public abstract class Bot {
    protected final ShipMap shipMap;

    public Bot(ShipMap shipMap) {
        this.shipMap = shipMap;
    }

    /**
     * The bot moves to an open neighbor.
     *
     * @return true if there is still an open path to the goal, otherwise false
     */
    abstract public boolean makeAMove();
}
