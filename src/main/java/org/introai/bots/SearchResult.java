package org.introai.bots;

import org.introai.Coordinate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class SearchResult {
    private final ArrayList<Coordinate> path;
    private final Coordinate start;
    private final Coordinate end;
    private final int distanceFromStart;

    public SearchResult(HashMap<Coordinate, Coordinate> parents, int distanceFromStart,
                        Coordinate start, Coordinate end) {
        this. distanceFromStart = distanceFromStart;
        this.start = start;
        this.end = end;
        this.path = constructPath(end, parents);
    }

    private ArrayList<Coordinate> constructPath(Coordinate end, HashMap<Coordinate, Coordinate> parents) {
        ArrayList<Coordinate> pathList = new ArrayList<>();
        Coordinate curr = end;
        while (curr != null) {
            pathList.add(curr);
            curr = parents.get(curr);
        }

        Collections.reverse(pathList);
        return pathList;
    }

    public ArrayList<Coordinate> getPath() {
        return path;
    }

    public Coordinate getStartLocation() {
        return start;
    }

    public Coordinate getEndLocation() {
        return end;
    }

    public int getDistanceFromStartToEnd() {
        return distanceFromStart;
    }
}
