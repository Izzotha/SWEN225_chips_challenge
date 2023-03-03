package nz.ac.vuw.ecs.swen225.gp22.app;

import static nz.ac.vuw.ecs.swen225.gp22.persistency.Persistence.loadLevel;
import static nz.ac.vuw.ecs.swen225.gp22.persistency.Persistence.saveLevel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import nz.ac.vuw.ecs.swen225.gp22.domain.Direction;
import nz.ac.vuw.ecs.swen225.gp22.domain.Game;
import nz.ac.vuw.ecs.swen225.gp22.domain.Level;
import nz.ac.vuw.ecs.swen225.gp22.domain.Position;
import nz.ac.vuw.ecs.swen225.gp22.renderer.GameView;


/**
 * Controller.
 *
 * <p> Handles the inputs from the user and updates the game accordingly </p>
 *
 * @author Luke Juriss
 */
public class Controller extends KeyAdapter {
  private GameView gv;
  private Game game;
  boolean ctrlPressed = false;
  public static int keyCount = 0;
  private Position previousPos;

  Controller(GameView gameView) {
    gv = gameView;
  }


  Direction keyDirection;

  @Override
  public void keyPressed(KeyEvent e) {
    assert SwingUtilities.isEventDispatchThread();
    switch (e.getKeyCode()) {
      case KeyEvent.VK_ESCAPE -> {
        Playgame.pauseButton.setText("Pause");
        Playgame.c.resume();
      }
      case KeyEvent.VK_CONTROL -> ctrlPressed = true;
      default -> { }
    }
    if (!Playgame.paused) {
      if (e.getKeyCode() == KeyEvent.VK_SPACE) {
        Playgame.pauseButton.setText("Resume");
        Playgame.c.pause();
      }
    }
    if (keyCount == 0 && !Playgame.paused && !Game.isGameOver()) {
      switch (e.getKeyCode()) {
        case KeyEvent.VK_UP -> {
          Game.movePlayer(Direction.UP);
          keyCount++;
          keyDirection = Direction.UP;
        }
        case KeyEvent.VK_DOWN -> {
          Game.movePlayer(Direction.DOWN);
          keyCount++;
          keyDirection = Direction.DOWN;

        }
        case KeyEvent.VK_LEFT -> {
          Game.movePlayer(Direction.LEFT);
          keyCount++;
          keyDirection = Direction.LEFT;
        }
        case KeyEvent.VK_RIGHT -> {

          Game.movePlayer(Direction.RIGHT);
          keyCount++;
          keyDirection = Direction.RIGHT;
        }
        default -> { }

      }
    }
    if (ctrlPressed) {
      switch (e.getKeyCode()) {
        case KeyEvent.VK_X -> {
          System.exit(0);
        }
        case KeyEvent.VK_S -> {
          saveLevel("./SavedLevels/LatestSave.xml", Game.currentLevel());
          System.exit(0);
        }
        case KeyEvent.VK_R -> {
          JFileChooser fileChooser = new JFileChooser("./");
          fileChooser.setDialogTitle("Select a level to load");
          int userSelection = fileChooser.showOpenDialog(gv);
          if (userSelection == JFileChooser.APPROVE_OPTION) {
            loadLevel(String.valueOf(fileChooser.getSelectedFile()));
          }
        }
        case KeyEvent.VK_1 -> Playgame.loadLevelNum(1);
        case KeyEvent.VK_2 -> Playgame.loadLevelNum(2);
        default -> { }
      }
    }

  }

  @Override
  public void keyReleased(KeyEvent e) {
    assert SwingUtilities.isEventDispatchThread();
    if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
      ctrlPressed = false;
    }
  }
}
