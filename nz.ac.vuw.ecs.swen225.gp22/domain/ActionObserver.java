package nz.ac.vuw.ecs.swen225.gp22.domain;

/**
 * This interface is used to observe the actions of an actor.
 * Other modules can take advantage of this to update their
 */
public interface ActionObserver {
    void onAction(ActionType action);
}
