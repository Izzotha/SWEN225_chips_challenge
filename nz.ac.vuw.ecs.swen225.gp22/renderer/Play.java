package nz.ac.vuw.ecs.swen225.gp22.renderer;

import java.awt.Graphics;
import nz.ac.vuw.ecs.swen225.gp22.domain.Game;

/**
 * Behavioral class which is utilized when the renderer needs to render the play screen.
 *
 * @author Andre Lepardo
 */
public class Play implements RenderStrategy {
  /**
   * This method is what the renderer will give display on screen, when the game is being played.
   *
   * @param g Graphics object responsible for drawing objects.
   * @param gameView GameView object to instruct.
   */
  public void execute(Graphics g, GameView gameView) {
    // Convert world positions to screen coordinates.
    gameView.convertToScreenCoordinates(Game.getTiles());
    gameView.drawWorld(g); // Draw the world
    if (gameView.needHelp()) { // If the GameView has
      // been instructed to draw the help text.
      gameView.displayHelp(Game.currentLevel().getLevelNumber());
      gameView.writeHelp(g);
    }
  }
}