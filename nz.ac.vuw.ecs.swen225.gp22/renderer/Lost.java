package nz.ac.vuw.ecs.swen225.gp22.renderer;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Behavioral class which is utilized when the renderer needs to render the paused screen.
 *
 * @author Andre Lepardo
 */
public class Lost implements RenderStrategy {

  /**
   * This method is what the renderer will give display on screen, when the game is being played.
   *
   * @param g Graphics object responsible for drawing objects.
   * @param gameView GameView object to instruct.
   */
  public void execute(Graphics g, GameView gameView) {
    // Setting up rendering parameters.
    g.drawImage(Image.Img.BACKGROUND.image, 0, 0,
            gameView.getScreenWidth() * 2, gameView.getScreenHeight() * 2, null);
    g.setColor(Color.RED.darker());
    assert CustomFont.FontType.SPACE.font != null;
    g.setFont(CustomFont.FontType.SPACE.font.deriveFont(50.0f));
    // Drawing assets on panel.
    g.drawString("YOU LOSE",
            (gameView.getScreenWidth() / 2) - 170,
            (gameView.getScreenHeight() / 2));
  }
}
