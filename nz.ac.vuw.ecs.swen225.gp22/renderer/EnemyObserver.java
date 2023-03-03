package nz.ac.vuw.ecs.swen225.gp22.renderer;

import nz.ac.vuw.ecs.swen225.gp22.domain.ActionObserver;
import nz.ac.vuw.ecs.swen225.gp22.domain.ActionType;

/**
 * This class is a class which plays a sound depending
 * on the ActionType is receives.
 */
public final class EnemyObserver implements ActionObserver {

  /**
   * Final sound object which does the actual loading and playing of the sounds.
   */
  private final Sound player = new Sound();

  @Override
  public void onAction(final ActionType action) {
    switch (action) {
      case TP -> player.playTp();
      case NONE -> System.out.println("Nothing!");
      default -> player.playSlime();
    }
  }
}
