package nz.ac.vuw.ecs.swen225.gp22.app;

import javax.swing.SwingUtilities;
/**
 * Main.
 *
 * <P> Main class for the game
 * calls the playGame method to begin the game </P>
 *
 * @author Luke Juriss
 */
public class Main {
  public static void main(String[] a) {
    SwingUtilities.invokeLater(Playgame::new);
  }
}
