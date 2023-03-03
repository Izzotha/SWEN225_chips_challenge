package nz.ac.vuw.ecs.swen225.gp22.renderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JPanel;
import nz.ac.vuw.ecs.swen225.gp22.domain.Actor;
import nz.ac.vuw.ecs.swen225.gp22.domain.Enemy;
import nz.ac.vuw.ecs.swen225.gp22.domain.Game;
import nz.ac.vuw.ecs.swen225.gp22.domain.Tile;


/**
 * This class is a JPanel responsible for drawing the state of the game.
 * It depends on the domain module's provided model of the game
 *
 * @author Andre Lepardo
 */
public final class GameView extends JPanel {

  /**
   * This field represents the width of the GameView panel.
   */
  private static final int SCREEN_WIDTH = 780;
  /**
   * This field represents the height of the GameView panel.
   */
  private static final int SCREEN_HEIGHT = 580;
  /**
   * This field represents the size of each unit on the GameView.
   */
  private static final int UNIT_SIZE = 80;
  /**
   * This field stores tile objects and their associated coordinate on the screen.
   */
  List<Pair<ScreenPoint, Tile>> tileList = new ArrayList<Pair<ScreenPoint, Tile>>();
  /**
   * This field stores the helpText for when the actor moves onto an info tile.
   */
  private String helpText = "";
  /**
   * This field determines or not to draw helpText.
   */
  private boolean help = false;
  /**
   * The game object which the renderer module reads from and draws.
   */
  private final Game game;
  /**
   * Strategy which is called upon to run when the game is in different states.
   */
  RenderStrategy strategy = new Play();

  /**
   * The public constructor for the GameView object.
   * Used by the Application Module.
   *
   * @param model The game which the GameView will render.
   */
  public GameView(Game model) {
    this.game = model;
    this.setPreferredSize(new Dimension(SCREEN_WIDTH,
            SCREEN_HEIGHT));
    this.setBackground(Color.black);
    this.setFocusable(true);
  }

  /**
   * Attaches ActionObservers to all the actors in the game.
   * The action observer will observe the actor and play sounds based
   * on what the actor does.
   *
   * @param g Graphics object which renders objects on screen.
   */
  public void initActionTypeListener(final Graphics g) {
    ActorObserver actorObserver = new ActorObserver();
    actorObserver.attach(this, new Pair<Boolean, Boolean>(true, false));
    Game.getPlayer().attach(actorObserver);
    Arrays.stream(Game.getEnemy()).forEach(e -> e.attach(new EnemyObserver()));
  }

  /**
   * Returns the game.
   *
   * @return game field.
   */
  public Game getGame() {
    return this.game;
  }

  /**
   * Returns the UNIT_SIZE.
   *
   * @return UNIT_SIZE.
   */
  public int getUnitSize() {
    return UNIT_SIZE;
  }

  /**
   * Returns the SCREEN_WIDTH.
   *
   * @return SCREEN_WIDTH.
   */
  public int getScreenWidth() {
    return SCREEN_WIDTH;
  }

  /**
   * Returns the SCREEN_HEIGHT.
   *
   * @return SCREEN_HEIGHT.
   */
  public int getScreenHeight() {
    return SCREEN_HEIGHT;
  }

  /**
   * Returns whether the game should be displaying the help text.
   *
   * @return help field.
   */
  public boolean needHelp() {
    return this.help;
  }

  /**
   * Sets the value of the help field.
   *
   * @param b value to set help field to.
   */
  public void setHelp(Boolean b) {
    this.help = b;
  }

  /**
   * Assigns strategy as pause strategy.
   */
  public void pause() {
    strategy = new Pause();
  }

  /**
   * Assigns strategy as unpause strategy.
   */
  public void unpause() {
    strategy = new Play();
  }

  /**
   * Assigns strategy as gameOver strategy.
   */
  public void gameOver() {
    strategy = new Lost();
  }

  /**
   * Assigns strategy as play strategy.
   */
  public void play() {
    strategy = new Play();
  }

  /**
   * Assigns strategy as rules strategy.
   */
  public void gameRules() {
    strategy = new Rules();
  }

  /**
   * Method which is called every time the JPanel is pinged to draw.
   * Utilizes other drawing methods to illustrate components
   * of the model separately.
   *
   * @param g Graphics object responsible for drawing objects onto the canvas.
   */
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    strategy.execute(g, this);
    initActionTypeListener(g);
  }

  /**
   * Utility method to display help text.
   *
   * @param level The current game level.
   */
  public void displayHelp(int level) throws IllegalArgumentException {
    if (level > 2 || level < 0) {
      throw new IllegalArgumentException("Invalid level");
    }
    if (level == 1) {
      this.helpText = """
              COLLECT KEYS TO OPEN
              THE DOORS. EAT ALL 
              BANANAS TO UNLOCK
              THE AIRLOCK BEFORE
              TIME RUNS OUT!
              """;
      return;
    }
    this.helpText = """
      DON'T TOUCH THE
      ECTOPLASM! THE
      SPACE GHOSTS CAN
      WALK THROUGH THEM,
      DON'T TOUCH THE 
      SPACE GHOSTS EITHER.
      """;
  }

  /**
   * Method which draws the help text onto the screen.
   *
   * @param g Graphics object responsible for drawing objects onto the canvas.
   */
  public void writeHelp(Graphics g) {
    g.setColor(Color.YELLOW.brighter().brighter());
    g.setFont(CustomFont.FontType.SPACE.font.deriveFont(37.0f));
    int textY = 100;
    for (String line : helpText.split("\\n")) {
      g.drawString(line, 50, textY);
      textY += 65;
    }
  }

  /**
   * This method is responsible for adapting game coordinates to screen coordinates.
   * It takes in the game board and converts the game coordinates to screen coordinates.
   *
   * @param tiles The tiles to be adapted and rendered.
   */
  public void convertToScreenCoordinates(Tile[][] tiles) {
    List<Pair<ScreenPoint, Tile>> list = new ArrayList<Pair<ScreenPoint, Tile>>();
    for (Tile[] tile : tiles) {
      for (Tile value : tile) {
        // The code below adapts the game positions to screen
        ScreenPoint newScreenPoint = CoordinateAdapter.getAdapter().convert(value.position(),
                Game.getPlayer().position(), this.getSize(),  UNIT_SIZE);
        list.add(new Pair<ScreenPoint, Tile>(newScreenPoint, value));
      }
    }
    this.tileList = list; // Setting the list field to be drawn.
  }

  /**
   * This method draws the actor onto the canvas.
   *
   * @param g            Graphics object responsible for drawing objets onto the canvas.
   * @param actor        Actor to draw.
   * @param screenPoint  ScreenPoint which represents the actors screen coordinates.
   */
  public void drawActor(final Graphics g, Actor actor, ScreenPoint screenPoint) {
    g.drawImage(ActorAnimator.getFinalAnimator().animateActor(actor),
            screenPoint.x(), screenPoint.y(), UNIT_SIZE, UNIT_SIZE, null);
  }

  /**
   * This method draws the enemy onto the canvas.
   *
   * @param g            Graphics object responsible for drawing objets onto the canvas.
   * @param enemy        Enemy to draw.
   * @param screenPoint  ScreenPoint which represents the actors screen coordinates.
   */
  public void drawEnemy(final Graphics g, Enemy enemy, ScreenPoint screenPoint) {
    g.drawImage(ActorAnimator.getFinalAnimator().animateEnemy(enemy, Game.PING_COUNT),
            screenPoint.x(), screenPoint.y(), UNIT_SIZE, UNIT_SIZE, null);
  }

  /**
   * This method is responsible for drawing the current state of the game.
   * It works by working through the 2d array of tiles which represent the world
   * and drawing them onto the canvas relative to the central focal point.
   *
   * @param g     Graphics object which does the drawing.
   */
  public void drawWorld(final Graphics g) {
    // Drawing the game world
    g.drawImage(Image.Img.BACKGROUND.image, 0, 0,
            SCREEN_WIDTH * 2, SCREEN_HEIGHT * 2, null);
    Dimension dimension = this.getSize();
    this.tileList.forEach(pair -> g.drawImage(Image.getTileImg(pair.v()),
            pair.k().x(), pair.k().y(), UNIT_SIZE, UNIT_SIZE, null));
    // Drawing the player
    drawActor(g, Game.getPlayer(), CoordinateAdapter.getAdapter()
            .convert(Game.getPlayer().position(), Game.getPlayer().position(),
                    dimension, UNIT_SIZE));
    // Drawing the enemies
    Arrays.stream(Game.getEnemy()).forEach(enemy ->
            drawEnemy(g, (Enemy) enemy, CoordinateAdapter.getAdapter()
                    .convert(enemy.position(), Game.getPlayer().position(), dimension, UNIT_SIZE)));
  }
}
