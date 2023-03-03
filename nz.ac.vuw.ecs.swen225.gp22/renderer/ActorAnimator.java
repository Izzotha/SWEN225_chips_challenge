package nz.ac.vuw.ecs.swen225.gp22.renderer;

import java.awt.image.BufferedImage;
import nz.ac.vuw.ecs.swen225.gp22.domain.Actor;
import nz.ac.vuw.ecs.swen225.gp22.domain.Enemy;

/**
 * This class is responsible for providing the images required to create the illusion of animation.
 *
 * @author Andre Lepardo
 */
public class ActorAnimator {
  /**
   * This field represents the frame of the animation to render.
   */
  private int frame = 0;
  /**
   * This field represents an identifier String which helps
   * to decide which image to fetch based on the state of the actor.
   */
  private String identifier = "";
  /**
   * Singleton object.
   */
  public static final ActorAnimator finalAnimator = new ActorAnimator();

  /**
   * Constructor is made private to ensure adherence to singleton pattern.
   */
  private ActorAnimator() {}

  /**
   * static method to return the lone animator object.
   *
   * @return The lone animator object.
   *
   */
  public static ActorAnimator getFinalAnimator() {
    return finalAnimator;
  }

  /**
   * This method decides which image to provide the images
   * to animate the actor as it moves within the maze.
   *
   * @param a The actor to be animated.
   * @return The buffered image to return for the current animation state.
   * @throws IllegalArgumentException Thrown when the actor passed in is null.
   */
  public BufferedImage animateActor(Actor a) throws IllegalArgumentException {
    if (a == null) { // If passed in actor is null.
      throw new IllegalArgumentException("Actor is null");
    }
    // The code below determines which image to return, based on the state of the actor.
    if (!a.hasMoved()) {
      return Image.getActorImg(a.direction(), "");
    }
    if (frame != 0) {
      identifier = "_" + frame;
      if (frame == 2) {
        frame = 0;
      }
    }
    frame++;
    return Image.getActorImg(a.direction(), identifier);
  }

  /**
   * This method is responsible for animating the enemy.
   *
   * @param a The enemy to be animated.
   * @param mod A variable which will help decide which animation frame to fetch.
   * @return The buffered image to return for the current animation state.
   * @throws IllegalArgumentException Thrown when the enemy passed in is null.
   */
  public BufferedImage animateEnemy(Enemy a, int mod) throws IllegalArgumentException {
    if (a == null) { // If the passed in enemy is null.
      throw new IllegalArgumentException("Enemy is null");
    }
    // Returns differing images which creates a 'blob' illusion.
    return mod % 5 == 0 ? Image.getEnemyImage(a.direction(), "") :
            Image.getEnemyImage(a.direction(), "_1");
  }
}