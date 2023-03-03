package nz.ac.vuw.ecs.swen225.gp22.fuzz;

import nz.ac.vuw.ecs.swen225.gp22.app.Playgame;
import nz.ac.vuw.ecs.swen225.gp22.domain.Direction;
import nz.ac.vuw.ecs.swen225.gp22.domain.Game;
import nz.ac.vuw.ecs.swen225.gp22.domain.Position;
import nz.ac.vuw.ecs.swen225.gp22.domain.Tile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * This class is used to test the game for any bugs that may occur.
 * It does this by randomly generating a game and then playing it.
 */
public class FuzzTest {
    Robot robot = new Robot();
    List<Position> oldPos = new ArrayList<>();
    Position newPos;
    boolean gamePaused = false;
    //list of keys to be pressed
    int[] directionKeys = new int[]{
            KeyEvent.VK_UP,
            KeyEvent.VK_DOWN,
            KeyEvent.VK_LEFT,
            KeyEvent.VK_RIGHT,
            // KeyEvent.VK_ESCAPE,
            //  KeyEvent.VK_SPACE
    };

    /**
     * Needs to throw AWTException to create a robot
     * @throws AWTException if robot cannot be created
     */
    public FuzzTest() throws AWTException {
    }

    /**
     * used to test lvl 1 of the game using the java robot to simulate key presses
     */
    @Test
    @Timeout(value = 60, unit = java.util.concurrent.TimeUnit.SECONDS)
    public void test1() {
        //create a new game
        createNewGame();
        // set the level in case it is not set
        setLevel(1);
        long startTime = System.currentTimeMillis() / 1000;
        //press random keys for 1 minute using robot pressing a key every 250ms
        for (int i = 0; i < 1000; i++) {
            int key = lookForObjects(i);
            movePlayer(key);
            System.out.println("Time: " + (System.currentTimeMillis() / 1000 - startTime));
        }
        //end of testing time
        robot.delay(500);
        runSaveFunctions();
    }

    /**
     * test level 2 of the game
     */
    @Test
    @Timeout(value = 60, unit = java.util.concurrent.TimeUnit.SECONDS)
    public void test2() {
        //create a new game
        createNewGame();
        // set the level in case it is not set
        setLevel(2);
        //press random keys for 1 minute using robot pressing a key every 250ms
        for (int i = 0; i < 800; i++) {
            int key = lookForObjects(i);
            movePlayer(key);
        }
        //end of testing time
        robot.delay(500);
        runSaveFunctions();
    }


    /**
     * used to set the game level using the java robot to simulate key presses
     * @param lvl the level to set the game to
     * @throws AWTException
     */
    private void setLevel(int lvl) {
        robot.delay(1000);
        if (lvl == 1) {
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_1);
            robot.keyRelease(KeyEvent.VK_1);
            robot.keyRelease(KeyEvent.VK_CONTROL);
        } else if (lvl == 2) {
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_2);
            robot.keyRelease(KeyEvent.VK_2);
            robot.keyRelease(KeyEvent.VK_CONTROL);
        }
    }

    /**
     * press the key to move the player
     *
     * @param randomKey the key to press
     */
    private void movePlayer(int randomKey) {
        if (!Game.isGameOver()) {
            if (!gamePaused) {
                try {
                    oldPos.add(Game.getPlayer().position());
                    robot.keyPress(randomKey);
                    robot.keyRelease(randomKey);
                    if (randomKey == KeyEvent.VK_SPACE) gamePaused = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (randomKey == KeyEvent.VK_ESCAPE) gamePaused = false;
            robot.delay(50);
        } else {//if game is over then restart the game
            System.out.println("Game Over");
            runRestartFunctions();
        }
    }

    /**
     * check if the player has moved position
     *
     * @param oldPos list of old positions
     * @param newPos new position
     * @return true if the player has moved position
     */
    private boolean hasMoved(List<Position> oldPos, Position newPos) {
        return !newPos.equals(oldPos.get(oldPos.size() - 1));
    }

    /**
     * find the objects that the player can interact with adds them to a list and returns the list
     * if available move is in the old position list then it is not added to the list
     *
     * @param oldPos list of old positions
     * @return list of keys that can be pressed
     */
    private int[] findObject(List<Position> oldPos) {
        return Stream.of(Direction.LEFT, Direction.DOWN, Direction.RIGHT, Direction.UP)
                .filter(dir -> {//filter out the tiles that are of unwanted type.
                    Tile t = Game.currentLevel().getTile(Game.getPlayer().position().add(dir.toPoint()));
                    return !t.name().equals("WALL") && !t.name().equals("FREE") && !t.name().equals("INFO");
                })//filter out the tiles that are in the old position list
                .filter(dir -> !oldPos.contains(Game.getPlayer().position().add(dir.toPoint())))
                .mapToInt(dir -> switch (dir) {//map the direction to the key that is used to press it
                    case UP -> KeyEvent.VK_UP;
                    case DOWN -> KeyEvent.VK_DOWN;
                    case LEFT -> KeyEvent.VK_LEFT;
                    case RIGHT -> KeyEvent.VK_RIGHT;
                })
                .toArray();
    }

    /**
     * simulates pressing keys to restart the game on death of the player
     */
    public void runRestartFunctions() {
        try {
            if(Game.currentLevel().getLevelNumber() == 1) {
                //press keys to restart the game
                robot.delay(500);
                robot.keyPress(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_1);
                robot.keyRelease(KeyEvent.VK_1);
                robot.keyRelease(KeyEvent.VK_CONTROL);
                System.out.println("Restarted Game");
            }else if(Game.currentLevel().getLevelNumber() == 2){
                //press keys to restart the game
                robot.delay(500);
                robot.keyPress(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_2);
                robot.keyRelease(KeyEvent.VK_2);
                robot.keyRelease(KeyEvent.VK_CONTROL);
                System.out.println("Restarted Game");
            }
        } catch (Exception e) {
            System.out.println("Error in restart process: ");
            e.printStackTrace();
        }
    }

    /**
     * used to save the game by simluating key presses
     */
    public void runSaveFunctions() {
        try {
            //press keys to save the game
            robot.delay(500);
            robot.mouseMove(850, 260);
            robot.mousePress(KeyEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(KeyEvent.BUTTON1_DOWN_MASK);
            System.out.println("Saved Game");
        } catch (Exception e) {
            System.out.println("Error in save process: ");
            e.printStackTrace();
        }
    }

    /**
     * used to debug the exit function only
     * and simluate the player exiting the game
     */
    public void runExitFunctions() {
        try {
            //press keys to exit the game by Pressing control + x
            robot.delay(500);
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_X);
            robot.keyRelease(KeyEvent.VK_X);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            System.out.println("Exited Game");
        } catch (Exception e) {
            System.out.println("Error in exit process: ");
            e.printStackTrace();
        }
    }

    /**
     * creates a new game, by invoking a new Playgame object
     * to start the game from the beginning
     * presses the keys to start the game
     */
    public void createNewGame() {
        try {
            SwingUtilities.invokeLater(Playgame::new);
            System.out.println("Game started");
            // Delay and wait for the game to load
            robot.delay(1500);
            //press Space to start the game
            if (Game.PING_COUNT == 0) {
                robot.keyPress(KeyEvent.VK_SPACE);
                robot.keyRelease(KeyEvent.VK_SPACE);
            }
            // set the level in case it is not set
        } catch (Exception e) {
            System.out.println("Cannot create showStructure: " + e);
        }
    }

    /**
     * used to find the objects that the player can interact with
     * will then get the random key from the directions in the list of possible directions
     * if nothing is found it will make return a key that is entirely random
     * @param loopNum the number of times the function has been called, used to print out the loop number
     * @return the key to press
     */
    private int lookForObjects(int loopNum) {
        newPos = Game.getPlayer().position();
        //make sure that the old position length is at a set max length
        if (oldPos.size() > 5) oldPos = oldPos.subList(oldPos.size() - 5, oldPos.size());
        int[] randomKeys = findObject(oldPos);

        //if an object was not found or player hasn't moved then randomly generate a key from the direction keys
        if (randomKeys.length == 0 || !hasMoved(oldPos, newPos)) {
            randomKeys = directionKeys;
        }
        int randomKey = randomKeys[(int) (Math.random() * randomKeys.length)];
        System.out.println(Arrays.toString(randomKeys));
        System.out.println("Action #: " + loopNum + " Key pressed: " + KeyEvent.getKeyText(randomKey));
        return randomKey;
    }
}


