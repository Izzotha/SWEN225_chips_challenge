package nz.ac.vuw.ecs.swen225.gp22.domain;

public interface TileType {

    /**
     * Checks if the tile is walkable.
     * @return true if the tile is walkable, false otherwise.
     */
    default boolean isWalkable(){
        return false;
    }

    /**
     * Interacts with the tile.
     * @return true if the interaction was successful, false otherwise.
     */
    default ActionType interact(Tile self, Action action){
        return ActionType.NONE;
    }

    /**
     * Undoes the interaction with the tile.
     * @param self the tile that is being interacted with.
     * @param action the action that is being undone.
     */
    default boolean undoInteract(Tile self, Action action){
        return false;
    }

    /**
     * Performs an action on an actor when they move onto the tile.
     * @return
     */
    default boolean onPing(Tile self, Actor actor){
        return false;
    }

    String toString();
    String name();
}
