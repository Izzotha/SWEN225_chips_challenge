package nz.ac.vuw.ecs.swen225.gp22.renderer;

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

/**
 * This class stores and plays the different sound effects and music required for the game.
 * The class contains basic file loading functions and functions to play and stop sounds.
 *
 * @author Andre Lepardo
 */
public class Sound {

  /**
   * public enum of different sound types, should be accessed by App module.
   */
  private enum SoundType {
    COLLECT,
    UNLOCK,
    WALLBUMP,
    NOTIF,
    MOVE,
    TP,
    SLIME,
    BGM;

    /**
     * Clip which is started to play the object's associated sound.
     */
    public final Clip clip;

    SoundType() {
      this.clip = getClip(name() + ".wav");
    }

    /**
     * This method is used by the constructor to load a wav file and craft a clip from it.
     * The clip is then returned and set as the object's clip field.
     * Only meant to be used by the constructor.
     *
     * @param fileName wav file to be loaded.
     * @return clip object to be played.
     */
    private Clip getClip(String fileName) {
      try {
        URL soundUrl = new URL("file:./Resources/Sounds/" + fileName);
        // Interacting with system audio functionalities.
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundUrl);
        Clip newClip = AudioSystem.getClip();
        newClip.open(audioInputStream);
        return newClip;
      } catch (RuntimeException e) { // For any potential exceptions.
        e.printStackTrace();
      } catch (Exception e) {
        System.out.println("Sound file reading failed.");
      }
      return null;
    }
  }

  /**
   * Play Move sound.
   */
  public void playMove() {
    Clip playClip = SoundType.MOVE.clip;
    assert playClip != null;
    playClip.setFramePosition(0);
    playClip.start();
  }

  /**
   * Play Notif sound.
   */
  public void playNotif() {
    Clip playClip = SoundType.NOTIF.clip;
    assert playClip != null;
    FloatControl volumeControl = (FloatControl) playClip.getControl(FloatControl.Type.MASTER_GAIN);
    volumeControl.setValue(4.0f); // Increase the volume by 4 decibels.
    playClip.setFramePosition(0);
    playClip.start();
  }

  /**
   * Play Unlock sound.
   */
  public void playUnlock() {
    Clip playClip = SoundType.UNLOCK.clip;
    assert playClip != null;
    playClip.setFramePosition(0);
    playClip.start();
  }

  /**
   * Play Collect sound.
   */
  public void playCollect() {
    Clip playClip = SoundType.COLLECT.clip;
    assert playClip != null;
    FloatControl volumeControl = (FloatControl) playClip.getControl(FloatControl.Type.MASTER_GAIN);
    volumeControl.setValue(-4.0f); // Decrease the volume by 4 decibels.
    playClip.setFramePosition(0);
    playClip.start();
  }

  /**
   * Play Wallbump sound.
   */
  public void playWallbump() {
    Clip playClip = SoundType.WALLBUMP.clip;
    assert playClip != null;
    FloatControl volumeControl = (FloatControl) playClip.getControl(FloatControl.Type.MASTER_GAIN);
    volumeControl.setValue(4.0f); // Increase the volume by 4 decibels.
    playClip.setFramePosition(0);
    playClip.start();
  }

  /**
   * Play Tp sound.
   */
  public void playTp() {
    Clip playClip = SoundType.TP.clip;
    assert playClip != null;
    FloatControl volumeControl = (FloatControl) playClip.getControl(FloatControl.Type.MASTER_GAIN);
    volumeControl.setValue(4.0f); // Increase the volume by 4 decibels.
    playClip.setFramePosition(0);
    playClip.start();
  }

  /**
   * Play Squish sound.
   */
  public void playSlime() {
    Clip playClip = SoundType.SLIME.clip;
    assert playClip != null;
    playClip.setFramePosition(0);
    playClip.start();
  }

  /**
   * Play Bgm sound.
   */
  public void playBgm() {
    Clip playClip = SoundType.BGM.clip;
    assert playClip != null;
    FloatControl volumeControl = (FloatControl) playClip.getControl(FloatControl.Type.MASTER_GAIN);
    volumeControl.setValue(-4.0f); // Decrease the volume by 4 decibels.
    playClip.setFramePosition(0);
    playClip.start();
    playClip.loop(Clip.LOOP_CONTINUOUSLY);
  }

  /**
   * Stops sound clips from playing.
   */
  public void stop() {
    try {
      AudioSystem.getClip().stop();
    } catch (Exception e) {
      System.out.println("Sound stopping failed.");
    }
  }

}

