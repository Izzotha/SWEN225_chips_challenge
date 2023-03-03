package nz.ac.vuw.ecs.swen225.gp22.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An action that can be performed on an actor.
 * Actions are performed by calling the apply() method.
 *
 * Author: Ninos Awas
 */
public class Action {
    private final Actor actor;
    private final Direction startDirection;
    private Direction endDirection;
    private int pingCount = -1; // The number of pings when this action was performed

    private Tile target;

    private Position startPosition;
    private Position endPosition;
    private boolean saveInteract;
    private boolean saveWalkable;

    /**
     * Create a new action.
     *
     * @param actor The actor to move.
     * @param direction The direction to move in.
     */
    public Action(Actor actor, Direction direction) {
        this.actor = actor;
        this.startDirection = actor.direction();
        this.endDirection = direction;
        this.startPosition = actor.position();
        this.endPosition = actor.position().add(direction.toPoint());
        this.target = Game.currentLevel().getTile(endPosition);
        this.pingCount = Game.PING_COUNT;
    }

    /**
     * Create an action from a save file.
     * @param actor The actor to perform the action on.
     * @param direction The direction to move in.
     * @param pingCount The number of pings when this action was performed.
     */
    public Action(Actor actor, Direction direction, int pingCount) {
        this(actor, direction);
        this.pingCount = pingCount;
    }

    /**
     * Apply this action to the actor.
     * @return True if the action was successful, false otherwise.
     *
     */
    public boolean apply(){

        if(this.pingCount != Game.PING_COUNT){ // This action was not created at the current ping count
            throw new IllegalStateException("Action was constructed with ping " + this.pingCount
                    + " but is being applied at ping " + Game.PING_COUNT);
        }

        actor.setDirection(endDirection);
        ActionType type = target.interact(this);
        if(type != ActionType.NONE) this.saveInteract = true;
        if(Game.isWalkable(endPosition)) {
            this.saveWalkable = true;
            if(type == ActionType.NONE) type = ActionType.MOVE;
            actor.setPosition(endPosition);
        }

        if(actor.getObserver() != null) // Notify ActionObserver of this action
            actor.getObserver().onAction(type);

        return saveInteract || saveWalkable; // Return true if the action was successful
    }



    /**
     * Undo this action.
     */
    public void undo(){
        actor.setDirection(startDirection);
        actor.setPosition(startPosition);
        if(saveInteract) target.undoInteract(this);
    }

    public Tile target() {
        return target;
    }

    public Actor getActor() {
        return actor;
    }

    public int pingCount() {
        return pingCount;
    }

    public Direction getEndDirection() {
        return endDirection;
    }

    public boolean hasMoved() {
        return saveWalkable;
    }
}
