package nz.ac.vuw.ecs.swen225.gp22.renderer;

import java.awt.Font;
import java.io.File;

/**
 * Class to load and store custom fonts.
 *
 * @author Andre Lepardo.
 */
public class CustomFont {

  /**
   * Custom font enum.
   */
  public enum FontType {
    SPACE;

    /**
     * Font field.
     */
    public final Font font;

    FontType() {
      font = loadFont(name() + ".ttf");
    }

    /**
     * Private loader only used by constructor.
     *
     * @param filename file to be loaded.
     * @return Custom font generated.
     */
    private Font loadFont(String filename) {
      try {
        return Font.createFont(Font.TRUETYPE_FONT,
                new File("./Resources/Fonts/" + filename));
      } catch (Exception e) {
        e.printStackTrace();
      }
      return null;
    }
  }

}
