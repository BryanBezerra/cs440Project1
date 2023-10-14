package org.introai.bots;

import org.introai.Coordinate;
import org.introai.ShipMap;
import org.introai.Search;
import org.introai.Simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Bot4 extends Bot {
    private ArrayList<Coordinate> plan;

    public Bot4(ShipMap shipMap) {
        super(shipMap);
    }

    /**
     * The bot moves to an open neighbor. It creates a new plan before each move
     * and uses its knowledge of how fast the fire is spreading to attempt to avoid
     * cells that will be burning by the time it gets there.
     *
     * @return
     */
    public boolean makeAMove() {
//        System.out.println(shipMap);
        if (!createPlan())
            return false;
        Coordinate curr = shipMap.getBotLocation();
        Coordinate nextMove = plan.get(1);
//        System.out.println("Moving to: " + nextMove);
        if (nextMove.equals(curr.getAbove())) shipMap.moveBotUp();
        else if (nextMove.equals(curr.getBelow())) shipMap.moveBotDown();
        else if (nextMove.equals(curr.getLeft())) shipMap.moveBotLeft();
        else if (nextMove.equals(curr.getRight())) shipMap.moveBotRight();
        else throw new RuntimeException("Invalid plan. This move is not possible.");
        return true;
    }

    /**
     * The bot creates a plan to get to the goal in the fewest possible moves. It simulates
     * the fire's future behavior and adds a priority penalty to dangerous cells.
     *
     * @return true if a route is possible, otherwise false
     */
    private boolean createPlan() {
        final int NUMBER_FIRE_SIMS = 20;
        final int TURNS_INTO_FUTURE = 20;
        Coordinate botStartLocation = shipMap.getBotLocation();
        Coordinate goalLocation = shipMap.getGoalLocation();
        HashMap<Coordinate, Double> dangerousCells =
                generateFireProbabilities(NUMBER_FIRE_SIMS, TURNS_INTO_FUTURE);
        SearchResult result;

        result = Search.fireSimAStarSearch(botStartLocation, goalLocation, dangerousCells, shipMap);
        if (result != null) {
            plan = result.getPath();
            return true;
        }
        return false;
    }

    /**
     * Generates the probability that fire will spread to cells in upcoming turns.
     * Cells with no observed ignition and cells ignited beforehand are not included
     * in the result.
     *
     * @param numberSimulations the number of simulations that will be averaged together
     * @param turnsIntoFuture how many turns into the future each simulation will look at
     * @return a map of the probability that each cell will ignite in the next turnsIntoFuture turns
     */
    private HashMap<Coordinate, Double> generateFireProbabilities(int numberSimulations, int turnsIntoFuture) {
        HashMap<Coordinate, Double> probabilities = new HashMap<>();
        for (int i = 0; i < numberSimulations; i++) {
            ShipMap shipCopy = shipMap.copyState();
            Simulation fireSim = new Simulation(shipCopy);
            HashSet<Coordinate> ignited = fireSim.firePrediction(turnsIntoFuture);
            for (Coordinate cell : ignited) {
                if (probabilities.containsKey(cell)) {
                    probabilities.put(cell, probabilities.get(cell) + 1.0);
                } else {
                    probabilities.put(cell, 1.0);
                }
            }
        }

        probabilities.replaceAll((c, v) -> probabilities.get(c) / numberSimulations);
        return probabilities;
    }
}
