package nz.ac.vuw.ecs.swen225.gp22.domain;

import java.util.*;

/**
 * A class that can find the shortest path between two points in the game.
 * Uses the breadth first search algorithm.
 *
 * @author Ninos Awas
 */

public class Pathfinder {

    /**
     * Gets the neighbouring tiles of a tile.
     * @param position the position of the tile.
     */
    private static List<Position> getNeighbours(Position position) {
        Level level = Game.currentLevel();
        List<Position> neighbours = new ArrayList<>();

        for (Direction d : Direction.values()) {
            Position neighbour = position.add(d.toPoint());
            // Make sure the neighbour is in the level
            if (neighbour.x() < 0 || neighbour.x() >= level.width() ) continue;
            if (neighbour.y() < 0 || neighbour.y() >= level.height()) continue;
            if(!level.getTile(neighbour).isWalkable()) continue;
            neighbours.add(neighbour);
        }
        return neighbours;
    }

    /**
     * Gets the direction to move in to get from the start position to the end position.
     */
    private static Direction getDirection(Position start, Position end) {
        if (start.x() < end.x()) return Direction.RIGHT;
        if (start.x() > end.x()) return Direction.LEFT;
        if (start.y() < end.y()) return Direction.DOWN;
        if (start.y() > end.y()) return Direction.UP;
        return null;
    }

    /**
     * Creates a list of directions that the actor must take to get to the target.
     * @param start the position of the actor.
     * @param target the position of the target.
     */
    public static Direction[] getPath(Position start, Position target) {
        Level level = Game.currentLevel();

        // The set of positions that have already been visited
        Set<Position> visited = new HashSet<>();
        // The queue of positions that need to be visited
        Queue<Position> queue = new ArrayDeque<>();
        // The map of positions to their parent positions
        Position[][] parent = new Position[level.width()][level.height()];

        // Add the starting position to the queue
        queue.add(start);
        visited.add(start);

        // While the queue is not empty
        while (!queue.isEmpty()) {
            // Get the next position in the queue
            Position position = queue.poll();

            // If the position is the target, then we have found the path
            if (position.equals(target)) {
                // Create a list of directions to return
                List<Direction> path = new ArrayList<>();
                // While the position is not the starting position
                while (!position.equals(start)) {
                    // Get the parent of the position
                    Position parentPosition = parent[(int) position.x()][(int) position.y()];
                    // Add the direction from the parent to the position to the path
                    path.add(getDirection(parentPosition, position));
                    // Set the position to the parent position
                    position = parentPosition;
                }
                // Reverse the path and return it
                Collections.reverse(path);
                return path.toArray(Direction[]::new);
            }


            // Get the positions of the neighbour tiles
            List<Position> neighbours = getNeighbours(position);

            // For each neighbour
            for (Position neighbour : neighbours) {
                // If the neighbour has not been visited
                if (!visited.contains(neighbour)) {
                    // Add the neighbour to the queue
                    queue.add(neighbour);
                    visited.add(neighbour);
                    parent[(int) neighbour.x()][(int) neighbour.y()] = position;
                }
            }
        }
        // If the queue is empty, then there is no path to the target
        return new Direction[0];
    }

    /**
     * Gets a random direction that is currently walkable from the given position.
     * @param position the position to start from.
     */
    public static Optional<Direction> getWalkableDirection(Position position) {
        // Get the neighbours of the position
        List<Position> neighbours = getNeighbours(position);

        // Get a random neighbour
        Position neighbour = neighbours.get(new Random().nextInt(neighbours.size()));
        return Optional.ofNullable(getDirection(position, neighbour));
    }

}
