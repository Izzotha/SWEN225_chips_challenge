package nz.ac.vuw.ecs.swen225.gp22.domain;

import java.util.List;
import java.util.Random;

/**
 * An enemy in the game.
 * The player must avoid enemies otherwise the game will end
 *
 * Enemies will move towards the player if they have a valid path
 * Otherwise they will move in random directions
 *
 * @author Ninos Awas
 */
public class Enemy extends Actor{

    private static final int PINGS_PER_MOVE = 15;

    public Enemy(double x, double y) {
        super(x, y);
    }

    /**
     * Moves the enemy in a random direction.
     */
    @Override
    public void ping() {
        if(Game.PING_COUNT % PINGS_PER_MOVE != 0) return;
        Direction[] path = Pathfinder.getPath(this.position(), Game.currentLevel().player().position());

        if(path.length < 1)
            Pathfinder.getWalkableDirection(this.position()).ifPresent(this::move);
        else
            this.move(path[0]);

        super.ping();
    }
}
