package nz.ac.vuw.ecs.swen225.gp22.renderer;

import nz.ac.vuw.ecs.swen225.gp22.domain.ActionObserver;
import nz.ac.vuw.ecs.swen225.gp22.domain.ActionType;

/**
 * Utility class which plays audio based on an Action event.
 *
 * @author Andre Lepardo.
 */
public final class ActorObserver implements ActionObserver {

  /**
   * Final sound object which does the actual loading and playing of the sounds.
   */
  private final Sound player = new Sound();
  /**
   * Wrapper pair object.
   */
  Pair<GameView, Pair<Boolean, Boolean>> pair;

  @Override
  public void onAction(ActionType action) {
    switch (action) {
      case MOVE -> { // No longer on info tile.
        player.playMove();
        pair.k().setHelp(pair.v().v()); // Instruct GameView to render help text.
      }
      case UNLOCK -> player.playUnlock();
      case COLLECT -> player.playCollect();
      case NOTIF -> { // On an info tile.
        player.playNotif();
        pair.k().setHelp(pair.v().k()); // Instruct GameView to render help text.
      }
      case SLIME -> player.playSlime();
      case TP -> player.playTp();
      case NONE -> player.stop();
      default -> player.playWallbump();
    }
  }

  /**
   * This method attaches a GameView object with a boolean pair.
   * Doing it in this manner prevents a malicious code
   * vulnerability.
   *
   * @param gameView GameView object to instruct.
   * @param doubleBoolean Boolean pair.
   */
  public void attach(GameView gameView, Pair<Boolean, Boolean> doubleBoolean) {
    pair = new Pair<GameView, Pair<Boolean, Boolean>>(gameView, doubleBoolean);
  }

}

