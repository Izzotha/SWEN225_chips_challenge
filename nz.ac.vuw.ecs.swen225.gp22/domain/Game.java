package nz.ac.vuw.ecs.swen225.gp22.domain;

import java.util.Arrays;
import java.util.List;

/**
 * Represent the game model.
 * This class controls the logic for the game
 *
 * Holds the current level
 *
 * Author: Ninos Awas
 */
public class Game {
    public static int PING_COUNT;
    private static Level[] levels;
    private static Level currentLevel;

    private static boolean gameOver;

    public Game(Level... levels){
        if(levels == null || levels.length == 0) throw new IllegalArgumentException("No levels provided.");
        Game.levels = levels;
        PING_COUNT = 0;
        currentLevel = levels[0]; //Set the current level to the first level in the list
        gameOver  = false;
    }

    public static boolean isWalkable(Position newPosition) {
        return currentLevel.getTile(newPosition).isWalkable();
    }

    public static void ping() {
        if(gameOver) return;

        pingActor(currentLevel.player());

        for(Actor enemy : currentLevel.enemies()){
            pingActor(enemy);
        }

        // Check if the player hit an enemy
        checkGameOver();

        PING_COUNT++;
    }

    private static void checkGameOver() {
        Tile playerTile = currentLevel().getTile(currentLevel().player().position());

        // Check if the player is on a tile with an enemy
        if(Arrays.stream(currentLevel().enemies())
                .anyMatch(enemy -> enemy.position().equals(playerTile.position()))){

            gameOver = true;

        }else if(playerTile.type() instanceof Tile.slime){
            gameOver = true;
        }
        else { // Check if the player is on the exit tile
            if(playerTile.type() instanceof Tile.exit){
                gameOver = true;
            }
        }
    }

    private static void pingActor(Actor actor) {
        Tile tile = currentLevel.getTile(actor.position());

        if(!tile.onPing(actor)){
            actor.ping();
        }else actor.clearAction();
    }

    public static Tile[][] getTiles() {
        return currentLevel.getTiles();
    }

    public static Actor getPlayer() {
        return currentLevel.player();
    }

    public static Actor[] getEnemy() {
        return currentLevel.enemies();
    }

    public static boolean movePlayer(Direction d) {
        getPlayer().move(d);
        return true;
    }

    public static void undo(){
        PING_COUNT--;
        currentLevel.player().undo();
    }

    public static void redo() {
        PING_COUNT++;
        currentLevel().player().redo();
    }

    public static Level currentLevel() {
        return currentLevel;
    }

    public static Level[] levels() {
        return levels;
    }

    public static boolean isGameOver() {
        return gameOver;
    }

    /**
     * Checks if the game is over and if the player is on the exit tile.
     * @return true if the game is over and the player is on the exit tile.
     *         false otherwise.
     */
    public static boolean hasWon(){
        return gameOver &&
                currentLevel().getTile(currentLevel().player().position()).type() instanceof Tile.exit;
    }

    public static void setGameOver(boolean gameOver) {
        Game.gameOver = gameOver;
    }

    /**
     * Checks if the player is on an info tile
     * @return true if the player is on an info tile
     */
    public static boolean showInfo(){
        return currentLevel().getTile(currentLevel().player().position()).type() instanceof Tile.info;
    }

    @Override
    public String toString() {
        return currentLevel.toString() + "\n Treasure count: " + currentLevel.treasureCount();
    }
}
