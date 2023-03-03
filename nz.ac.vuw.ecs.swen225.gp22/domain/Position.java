package nz.ac.vuw.ecs.swen225.gp22.domain;

public record Position(double x, double y) {

    public Position add(Position p) {
        return new Position(x + p.x, y + p.y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }


    @Override
    public boolean equals(Object other) {
        return other instanceof Position p && x == p.x && y == p.y;
    }

    @Override
    public int hashCode() {
        return (int) (x + y);
    }


}
