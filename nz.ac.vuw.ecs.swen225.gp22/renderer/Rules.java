package nz.ac.vuw.ecs.swen225.gp22.renderer;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Behavioral class which is utilized when the renderer needs to render the Rules screen.
 *
 * @author Andre Lepardo
 */
public class Rules implements RenderStrategy {
  /**
   * This method is what the renderer will give display on screen, when the game is being played.
   *
   * @param g Graphics object responsible for drawing objects.
   * @param gameView GameView object to instruct.
   */
  public void execute(Graphics g, GameView gameView) {
    g.drawImage(Image.getRules(), 0, 0,
            gameView.getScreenWidth(), gameView.getScreenHeight(), null);

    String rules = """
            Your human handlers have left you alone
            and hungry aboard their Space Station.
            Eat all the bananas you can find before
            you die of starvation.
            Use the arrow keys to navigate your way
            through the station, collecting
            keys, unlocking doors and eating bananas.
            
            Beware, you might not be alone up here...
            """;

    g.setColor(Color.YELLOW);
    g.setFont(CustomFont.FontType.SPACE.font);
    int textY = 50;
    int textX = 20;
    for (String line : rules.split("\\n")) { // Draw the text on the screen.
      g.drawString(line, textX, textY);
      textY += 35;
    }
  }
}
