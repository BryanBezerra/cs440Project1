package org.introai.bots;

import org.introai.Coordinate;
import org.introai.PriorityCoordinate;
import org.introai.Search;
import org.introai.ShipMap;

import java.util.HashMap;
import java.util.PriorityQueue;

public class Bot1 extends Bot {
    private boolean firstRun;
    private Coordinate[] plan;

    public Bot1(ShipMap shipMap) {
        super(shipMap);
        this.firstRun = true;
    }

    public boolean makeAMove() {
        if (firstRun) {
            Coordinate botStartLocation = shipMap.getBotLocation();
            Coordinate goalLocation = shipMap.getGoalLocation();
            SearchResult result = Search.aStarSearch(botStartLocation, goalLocation, shipMap);
            if (result != null) plan = result.getPath();
            else return false;
            firstRun = false;
        }
        //TODO Move in a direction

        return true;
    }


}
