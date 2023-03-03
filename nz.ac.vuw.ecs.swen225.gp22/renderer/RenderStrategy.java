package nz.ac.vuw.ecs.swen225.gp22.renderer;

import java.awt.Graphics;
import java.io.Serializable;

/**
 * Common interface for renderer strategies.
 *
 * @author Andre Lepardo.
 */
public interface RenderStrategy extends Serializable {
  void execute(Graphics g, GameView gameView);
}

