package org.introai;

import org.introai.bots.SearchResult;

import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

public class Search {

    /**
     * Searches for the shortest path from the start to the goal.
     * Returns an array of Coordinates containing that path.
     *
     * @param start the starting Coordinate
     * @param goal the goal Coordinate
     * @return a SearchResult object containing the shortest path and distance to goal
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
            if (curr.equals(goal)) {
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

    /**
     * Searches for the shortest path from the start to the goal. It completely avoids
     * cells adjacent to tiles that are on fire.
     * Returns an array of Coordinates containing that path.
     *
     * @param start the starting Coordinate
     * @param goal the goal Coordinate
     * @param shipMap
     * @return a SearchObject containing the path found
     */
    public static SearchResult aStarSearchAvoidFire(Coordinate start, Coordinate goal,
                                           ShipMap shipMap) {
        PriorityQueue<PriorityCoordinate> fringe = new PriorityQueue<>();
        HashMap<Coordinate, Integer> distanceFromStart = new HashMap<>();
        HashMap<Coordinate, Coordinate> parents = new HashMap<>();
        HashSet<Coordinate> fireAdjacent = adjacentToFire(shipMap);
        boolean found = false;

        distanceFromStart.put(start, 0);
        fringe.add(new PriorityCoordinate(start, 0));
        parents.put(start, null);

        while (!fringe.isEmpty()) {
            Coordinate curr = fringe.poll().getCoordinate();
            if (curr.equals(goal)) {
                found = true;
                break;
            }

            Coordinate[] neighbors = shipMap.openNeighbors(curr);
            for (Coordinate neighbor : neighbors) {
                if (fireAdjacent.contains(neighbor)) {
                    continue;
                }
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

    /**
     * Searches for the shortest path from the start to the goal. It attempts to avoid paths that might
     * ignite by weighing cells that simulations suggest are dangerous more harshly.
     *
     * @param start the starting Coordinate
     * @param goal the goal Coordinate
     * @param dangerousCells a map of cells that have been simulated to be in danger of fire
     * @param shipMap the ship's state
     * @return a SearchObject containing the path found
     */
    public static SearchResult fireSimAStarSearch(Coordinate start, Coordinate goal,
                                                  HashMap<Coordinate, Double> dangerousCells, ShipMap shipMap) {
        PriorityQueue<PriorityCoordinate> fringe = new PriorityQueue<>();
        HashMap<Coordinate, Integer> distanceFromStart = new HashMap<>();
        HashMap<Coordinate, Coordinate> parents = new HashMap<>();
        final double DANGER_PENALTY = 10.0;
        boolean found = false;

        distanceFromStart.put(start, 0);
        fringe.add(new PriorityCoordinate(start, 0));
        parents.put(start, null);

        while (!fringe.isEmpty()) {
            Coordinate curr = fringe.poll().getCoordinate();
            if (curr.equals(goal)) {
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
                    if (dangerousCells.containsKey(neighbor)) {
                        int additionalPriority = (int) (dangerousCells.get(neighbor) * DANGER_PENALTY);
                        priority += additionalPriority;
                    }
                    fringe.add(new PriorityCoordinate(neighbor, priority));
                    parents.put(neighbor, curr);
                }
            }
        }

        if (!found) return null;
        return new SearchResult(parents, distanceFromStart.get(goal), start, goal);
    }

    /**
     * Finds all open cells that are adjacent to burning cells.
     *
     * @param shipMap the ship's state
     * @return a set of all open cells adjacent to burning cells
     */
    private static HashSet<Coordinate> adjacentToFire(ShipMap shipMap) {
        HashSet<Coordinate> fireCells = shipMap.getFireCells();
        HashSet<Coordinate> openCells = shipMap.getOpenCells();
        HashSet<Coordinate> adjacentToFireCells = new HashSet<>();

        for (Coordinate cell : fireCells) {
            Coordinate[] neighbors = {cell.getAbove(), cell.getBelow(), cell.getLeft(), cell.getRight()};
            for (Coordinate neighbor : neighbors) {
                if (openCells.contains(neighbor)) adjacentToFireCells.add(neighbor);
            }
        }
        return adjacentToFireCells;
    }

    /**
     * Calculates the Manhattan distance between two Coordinates
     *
     * @param coordinateA a Coordinate
     * @param coordinateB a Coordinate
     * @return the Manhattan distance between the two coordinates
     */
    private static int manhattanDistance(Coordinate coordinateA, Coordinate coordinateB) {
        int[] a = coordinateA.toArray();
        int[] b = coordinateB.toArray();
        return Math.abs(a[0] - b[0]) + Math.abs(a[1] - b[1]);
    }
}
