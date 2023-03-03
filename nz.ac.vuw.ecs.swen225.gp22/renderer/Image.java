package nz.ac.vuw.ecs.swen225.gp22.renderer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import nz.ac.vuw.ecs.swen225.gp22.domain.Direction;
import nz.ac.vuw.ecs.swen225.gp22.domain.Tile;


/**
 * Image
 * This class is responsible for loading and storing image files to be used in the rendering
 * of the game. This class contains methods for loading images, and fetching the corresponding
 * image for game tiles and actors.
 *
 * @author Andre Lepardo
 */
public final class Image {
  /**
   * Img enum, based off of Assignment 1 code.
   */
  public enum Img {
    DOWN, EXIT, FREE, INFO, LEFT, TP, SLIME, TITLE,
    RIGHT, TREASURE, UP, WALL, ENEMY_DOWN, ENEMY_DOWN_1, ENEMY_UP, ENEMY_UP_1,
    ENEMY_LEFT, ENEMY_LEFT_1, ENEMY_RIGHT, ENEMY_RIGHT_1, BLUE_DOOR, BLUE_KEY, RED_DOOR,
    RED_KEY, GREEN_DOOR, GREEN_KEY, YELLOW_DOOR, YELLOW_KEY, BACKGROUND, RULES,
    EXIT_LOCK, UP_1, UP_2, DOWN_1, DOWN_2, LEFT_1, LEFT_2, RIGHT_1, RIGHT_2;
    public final BufferedImage image;

    Img() {
      image = loadImage(this.name());
    }

    /**
     * This method loads the image corresponding to the given name parameter.
     *
     * @param name of the image to be loaded.
     * @return the loaded buffered image.
     * @throws Error URL filepath is null.
     */
    private static BufferedImage loadImage(String name) throws Error {
      try {
        URL hs = new URL("file:./Resources/Images/" + name + ".png");
        return ImageIO.read(hs);
      } catch (IOException e) {
        throw new Error(e);
      }
    }
  }

  /**
   * This method is provided for the Application module, to obtain the start screen image.
   *
   * @return The title page image.
   */
  public static BufferedImage getTitleScreenImage() {
    return Img.TITLE.image;
  }

  /**
   * This method is provided for the Application module, to obtain the rule screen
   * image.
   *
   * @return The rule page image.
   */
  public static BufferedImage getRules() {
    return Img.RULES.image;
  }

  /**
   * This is a utility method which fetches the
   * corresponding image of the given Direction parameter.
   *
   * @param d     Actor image to be returned.
   * @param frame The current frame in the animation.
   * @return The image corresponding to the direction.
   * @throws IllegalArgumentException Thrown when a tile with no matching image is passed in.
   */
  public static BufferedImage getActorImg(Direction d, String frame)
          throws IllegalArgumentException {
    String name = d.name() + frame;
    return switch (name) {
      case "UP" -> Img.UP.image;
      case "UP_1" -> Img.UP_1.image;
      case "UP_2" -> Img.UP_2.image;
      case "DOWN" -> Img.DOWN.image;
      case "DOWN_1" -> Img.DOWN_1.image;
      case "DOWN_2" -> Img.DOWN_2.image;
      case "LEFT" -> Img.LEFT.image;
      case "LEFT_1" -> Img.LEFT_1.image;
      case "LEFT_2" -> Img.LEFT_2.image;
      case "RIGHT" -> Img.RIGHT.image;
      case "RIGHT_1" -> Img.RIGHT_1.image;
      case "RIGHT_2" -> Img.RIGHT_2.image;
      default -> throw new IllegalArgumentException("No img found");
    };
  }

  /**
   * This is a utility method which fetches the
   * corresponding image of the given Direction parameter.
   *
   * @param d     Enemy image to be returned.
   * @param frame The current frame in the animation.
   * @return The image corresponding to the direction.
   * @throws IllegalArgumentException Thrown when no image could be found.
   */
  public static BufferedImage getEnemyImage(Direction d, String frame)
          throws IllegalArgumentException {
    String name = d.name() + frame;
    return switch (name) {
      case "UP" -> Img.ENEMY_UP.image;
      case "UP_1" -> Img.ENEMY_UP_1.image;
      case "DOWN" -> Img.ENEMY_DOWN.image;
      case "DOWN_1" -> Img.ENEMY_DOWN_1.image;
      case "LEFT" -> Img.ENEMY_LEFT.image;
      case "LEFT_1" -> Img.ENEMY_LEFT_1.image;
      case "RIGHT" -> Img.ENEMY_RIGHT.image;
      case "RIGHT_1" -> Img.ENEMY_RIGHT_1.image;
      default -> throw new IllegalArgumentException("No img found");
    };
  }

  /**
   * This is a utility method which fetches the corresponding image of the given tile parameter.
   *
   * @param t The tile image to be returned.
   * @return The image corresponding to the given tile.
   * @throws IllegalArgumentException Thrown when a tile with no matching image is passed in.
   */
  public static BufferedImage getTileImg(Tile t)
          throws IllegalArgumentException {
    String name = t.name();
    return switch (name) {
      case "WALL" -> Img.WALL.image;
      case "FREE" -> Img.FREE.image;
      case "TREASURE" -> Img.TREASURE.image;
      case "INFO" -> Img.INFO.image;
      case "EXIT" -> Img.EXIT.image;
      case "EXIT_LOCK" -> Img.EXIT_LOCK.image;
      case "TP" -> Img.TP.image;
      case "SLIME" -> Img.SLIME.image;
      case "BLUE_DOOR" -> Img.BLUE_DOOR.image;
      case "BLUE_KEY" -> Img.BLUE_KEY.image;
      case "GREEN_DOOR" -> Img.GREEN_DOOR.image;
      case "GREEN_KEY" -> Img.GREEN_KEY.image;
      case "RED_DOOR" -> Img.RED_DOOR.image;
      case "RED_KEY" -> Img.RED_KEY.image;
      case "YELLOW_DOOR" -> Img.YELLOW_DOOR.image;
      case "YELLOW_KEY" -> Img.YELLOW_KEY.image;
      default -> throw new IllegalArgumentException("No img found");
    };
  }
}
