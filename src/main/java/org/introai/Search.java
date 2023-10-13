package org.introai;

import org.introai.bots.SearchResult;

import java.util.HashMap;
import java.util.PriorityQueue;

public class Search {

    /**
     * Searches for a shortest path from the start to the goal.
     * Returns an array of Coordinates containing that path.
     *
     * @param start the starting Coordinate
     * @param goal the goal Coordinate
     * @return an object containing the shortest path and distance to goal
     */
    public static SearchResult aStarSearch(Coordinate start, Coordinate goal,
                                           ShipMap shipMap) {
        PriorityQueue<PriorityCoordinate> fringe = new PriorityQueue<>();
        HashMap<Coordinate, Integer> distanceFromStart = new HashMap<>();
        HashMap<Coordinate, Coordinate> parents = new HashMap<>();
        boolean found = false;

        distanceFromStart.put(start, 0);
        fringe.add(new PriorityCoordinate(start, 0));
        parents.put(start, null);

        while (!fringe.isEmpty()) {
            Coordinate curr = fringe.poll().getCoordinate();
            if (curr == goal) {
                found = true;
                break;
            }

            Coordinate[] neighbors = shipMap.openNeighbors(curr);
            for (Coordinate neighbor : neighbors) {
                int tempDistance = distanceFromStart.get(curr) + 1;
                if (!distanceFromStart.containsKey(neighbor) ||
                        tempDistance < distanceFromStart.get(neighbor)) {
                    distanceFromStart.put(neighbor, tempDistance);
                    int priority = tempDistance + manhattanDistance(neighbor, goal);
                    fringe.add(new PriorityCoordinate(neighbor, priority));
                    parents.put(neighbor, curr);
                }
            }
        }

        if (!found) return null;
        return new SearchResult(parents, distanceFromStart.get(goal), start, goal);
    }

    private static int manhattanDistance(Coordinate coordinateA, Coordinate coordinateB) {
        int[] a = coordinateA.toArray();
        int[] b = coordinateB.toArray();
        return Math.abs(a[0] - b[0]) + Math.abs(a[1] - b[1]);
    }
}
