package org.introai;

import java.util.Objects;

public class PriorityCoordinate implements Comparable<PriorityCoordinate> {
    private final Coordinate coordinate;
    private final int priority;

    public PriorityCoordinate(Coordinate coordinate, int priority) {
        this.coordinate = coordinate;
        this.priority = priority;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    @Override
    public String toString() {
        return "[" + priority + ", " + coordinate + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinate, priority);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;
        final PriorityCoordinate other = (PriorityCoordinate) obj;
        return this.coordinate == other.coordinate && this.priority == other.priority;
    }

    @Override
    public int compareTo(PriorityCoordinate o) {
        return this.priority - o.priority;
    }
}
