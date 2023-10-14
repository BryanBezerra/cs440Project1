package org.introai.bots;

import org.introai.Coordinate;
import org.introai.Search;
import org.introai.ShipMap;

import java.util.ArrayList;

public class Bot2 extends Bot {
    private ArrayList<Coordinate> plan;

    public Bot2(ShipMap shipMap) {
        super(shipMap);
    }

    /**
     * The bot moves to an open neighbor. It creates a new plan before each move.
     *
     * @return true if there is still an open path to the goal, otherwise false
     */
    public boolean makeAMove() {
        if (!createPlan())
            return false;
        Coordinate curr = shipMap.getBotLocation();
        Coordinate nextMove = plan.get(1);
        if (nextMove.equals(curr.getAbove())) shipMap.moveBotUp();
        else if (nextMove.equals(curr.getBelow())) shipMap.moveBotDown();
        else if (nextMove.equals(curr.getLeft())) shipMap.moveBotLeft();
        else if (nextMove.equals(curr.getRight())) shipMap.moveBotRight();
        else throw new RuntimeException("Invalid plan. This move is not possible.");
        return true;
    }

    /**
     * The bot creates a plan to get to the goal in the fewest possible moves.
     *
     * @return true if a route is possible, otherwise false
     */
    private boolean createPlan() {
        Coordinate botStartLocation = shipMap.getBotLocation();
        Coordinate goalLocation = shipMap.getGoalLocation();
        SearchResult result = Search.aStarSearch(botStartLocation, goalLocation, shipMap);
        if (result != null) {
            plan = result.getPath();
            return true;
        }
        return false;
    }
}
