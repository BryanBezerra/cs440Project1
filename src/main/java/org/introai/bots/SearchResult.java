package org.introai.bots;

import org.introai.Coordinate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class SearchResult {
    private final Coordinate[] path;
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

    private Coordinate[] constructPath(Coordinate end, HashMap<Coordinate, Coordinate> parents) {
        ArrayList<Coordinate> pathList = new ArrayList<>();
        Coordinate curr = end;
        while (curr != null) {
            pathList.add(curr);
            curr = parents.get(curr);
        }

        Collections.reverse(pathList);
        return (Coordinate[]) pathList.toArray();
    }

    public Coordinate[] getPath() {
        return path;
    }

    public int getDistanceFromStart() {
        return distanceFromStart;
    }
}