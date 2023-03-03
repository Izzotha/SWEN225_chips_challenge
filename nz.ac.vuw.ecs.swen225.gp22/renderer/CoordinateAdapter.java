package nz.ac.vuw.ecs.swen225.gp22.renderer;

import java.awt.Dimension;
import nz.ac.vuw.ecs.swen225.gp22.domain.Position;

/**
 * Utility class to adapt world coordinates to screen coordinates.
 *
 * @author Andre Lepardo.
 */
public final class CoordinateAdapter {

  /**
   * Lone coordinate adapter object.
   */
  private static final CoordinateAdapter ADAPTER = new CoordinateAdapter();

  /**
   * Private constructor to adhere to singleton pattern.
   */
  private CoordinateAdapter() {}

  /**
   * Retrieves the lone adapter object.
   *
   * @return The lone adapter object.
   */
  public static CoordinateAdapter getAdapter() {
    return ADAPTER;
  }

  /**
   * Method which returns the screen coordinate of a given tile object.
   *
   * @param p Game position to be adapted.
   * @param playerPosition Position to calculate around.
   * @param dimension current dimensions of the GameView.
   * @param unitSize unitSize of each object to be drawn in the screen.
   * @return Screen Coordinate of the tile object.
   */
  public ScreenPoint convert(Position p, Position playerPosition,
                             Dimension dimension, int unitSize) {
    // Calculating screen x position relative to the world position.
    int screenX = ((int) p.x() * unitSize - (int) playerPosition.x()
            * unitSize + (int) dimension.getWidth() / 2) - (unitSize / 2);
    // Calculating screen y position relative to the world position.
    int screenY = ((int) p.y() * unitSize - (int) playerPosition.y()
            * unitSize + (int) dimension.getHeight() / 2) - (unitSize / 2);
    return  new ScreenPoint(screenX, screenY);
  }

}
