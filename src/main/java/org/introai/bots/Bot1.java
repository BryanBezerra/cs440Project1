package org.introai.bots;

import org.introai.Coordinate;
import org.introai.PriorityCoordinate;
import org.introai.ShipMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Bot1 extends Bot {
    private final Coordinate goalLocation;

    public Bot1(ShipMap shipMap) {
        super(shipMap);
        this.goalLocation = shipMap.getGoalLocation();
    }

    public boolean makeAMove() {
        Coordinate botLocation = shipMap.getBotLocation();
        return true;
    }

    /**
     * Searches for a shortest path from the start to the goal. Returns an
     * array of Coordinates containing that path.
     *
     * @param start the starting Coordinate
     * @param goal the goal Coordinate
     * @return an object containing the shortest path and shortest distances found
     */
    private SearchResult aStarSearch(Coordinate start, Coordinate goal) {
        PriorityQueue<PriorityCoordinate> fringe = new PriorityQueue<>();
        HashMap<Coordinate, Integer> distanceFromStart = new HashMap<>();
        HashMap<Coordinate, Coordinate> parents = new HashMap<>();
        boolean found = false;

        distanceFromStart.put(start, 0);
        fringe.add(new PriorityCoordinate(start, 0));
        parents.put(start, null);

        while (!fringe.isEmpty()) {
            Coordinate curr = fringe.poll().getCoordinate();
            if (curr == goalLocation) {
                found = true;
                break;
            }

            Coordinate[] neighbors = shipMap.openNeighbors(curr);
            for (Coordinate neighbor : neighbors) {
                int tempDistance = distanceFromStart.get(curr) + 1;
                if (!distanceFromStart.containsKey(neighbor) ||
                        tempDistance < distanceFromStart.get(neighbor)) {
                    distanceFromStart.put(neighbor, tempDistance);
                    fringe.add(neighbor, tempDistance + distanceEstimate(neighbor));
                    parents.put(neighbor, curr);
                }
            }
        }

        if (!found) return null;

        return constructPath()
    }
}
