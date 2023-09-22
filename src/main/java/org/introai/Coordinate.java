package org.introai;

import java.util.HashSet;
import java.util.Objects;

public class Coordinate {
    private final int x;
    private final int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate getAbove() {
        return new Coordinate(x, y + 1);
    }

    public Coordinate getBelow() {
        return new Coordinate(x, y - 1);
    }

    public Coordinate getRight() {
        return new Coordinate(x + 1, y);
    }

    public Coordinate getLeft() {
        return new Coordinate (x - 1, y);
    }

    public int[] toArray() {
        return new int[]{this.x, this.y};
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;
        final Coordinate other = (Coordinate) obj;
        return this.x == other.x && this.y == other.y;
    }

    public static void main(String[] args) {
        Coordinate a = new Coordinate(2, 4);
        Coordinate b = new Coordinate(2,3);
        Coordinate c = new Coordinate(2, 4);
        HashSet<Coordinate> test = new HashSet<>();
        System.out.println(a.equals(c));
        test.add(a);
        test.add(b);
        test.add(c);
        for (Coordinate coordinate : test) {
            System.out.println(coordinate);
        }
    }
}
