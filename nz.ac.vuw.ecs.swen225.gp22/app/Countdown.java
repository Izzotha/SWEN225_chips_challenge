package nz.ac.vuw.ecs.swen225.gp22.app;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import javax.swing.*;
import nz.ac.vuw.ecs.swen225.gp22.domain.Game;
import nz.ac.vuw.ecs.swen225.gp22.recorder.Recorder;
import nz.ac.vuw.ecs.swen225.gp22.renderer.GameView;

import static nz.ac.vuw.ecs.swen225.gp22.persistency.Persistence.saveLevel;


/**
 * Countdown.
 * This class is the countdown timer for the game,
 * also handles screen updates 30 frames a second and minor game logic.
 *
 * @author Luke Juriss
 */
public class Countdown {
  private final static int PING_DELAY = 66;
  private Timer timer;
  private long startTime = -1;
  private long stopTime = -1;
  private long duration;
  private GameView gv;
  Playgame self;

  private int prevPingTreasure;
  private int count;

  /**
   * Constructor for the Countdown class.
   *
   * @param gameview the GameView object
   * @param self     the playGame object
   * @param duration the duration of the timer in milliseconds
   */
  public Countdown(Playgame self, GameView gameview, int duration) {
    this.duration = duration;
    this.gv = gameview;
    this.self = self;

    timer = new Timer(PING_DELAY, new ActionListener() { //66 milliseconds is 30 frames per second
      @Override
      public void actionPerformed(ActionEvent e) { //every 66 milliseconds, this method is called
        Controller.keyCount = 0;  //reset keyCount
         if(Game.hasWon()){
           if(Game.currentLevel().getLevelNumber() == 1){
             Playgame.loadLevelNum(2);
           }
           else{
             JFileChooser fileChooser = new JFileChooser("./RecordedGames/");
             fileChooser.setDialogTitle("You Win, Save your recorded Game");
             int userSelection = fileChooser.showSaveDialog(gv);
             if (userSelection == JFileChooser.APPROVE_OPTION) {
               Recorder.saveGame(gv.getGame(), fileChooser.getSelectedFile());
             }
             Playgame.loadLevelNum(1);
           }

         }
        else if(Game.isGameOver()){
          gv.gameOver();
          count++;
          if(count == 3){
            Playgame.loadLevelNum(Game.currentLevel().getLevelNumber());
          }
        }
        else{
           count = 0;
         }


        long currentKeys = Game.currentLevel() //Calculate num of key in the players inventory
                .player().inventory()
                .stream()
                .filter(i -> (i.toString().equals("r")) || (i.toString().equals("g"))
                        || (i.toString().equals("b")) || (i.toString().equals("y")))
                .count();
        Playgame.keysCollected.setText(" Keys Collected: " + currentKeys); //update the keys collected label
        //A key must've been used so play the sound effect
        prevPingTreasure = Game.currentLevel().treasureCount();
        //Update the treasure remaining stat
        Playgame.treasureRemaining.setText(" Bananas Remaining: "
                + prevPingTreasure); // update the treasure remaining label

        //Update the level Number stat
        Playgame.levelNum.setText(" Level: "
                + Game.currentLevel().getLevelNumber()); //update the level number label

        Game.ping();
        gv.repaint();

        if (startTime < 0) {
          startTime = System.currentTimeMillis(); //Get time timer started
        }
        long now = System.currentTimeMillis(); //Get current time
        long clockTime;
        clockTime = now - startTime; //Clock displays time since timer started
        if (clockTime >= duration) {
          clockTime = duration; //If the clock has reached the duration, stop the timer
          Game.setGameOver(true);
          gv.gameOver();
          gv.repaint();
          timer.stop();//Stop the timer
        }

        SimpleDateFormat df = new SimpleDateFormat("m:ss"); //Format the time
        self.clock.setText(" Time Remaining: "
                + df.format(duration - clockTime)); //Set the label to the formatted time
      }
    });
      self.requestFocus();
  }

  /**
   * Starts the timer.
   */
  public void start() {
    timer.start();
    self.requestFocus();
  }

  /**
   * Stops the timer.
   * Displays pause screen.
   */
  public void pause() {
    if (!Game.isGameOver() && !Playgame.paused) { //If the game is not lost or paused
      Playgame.paused = true; //Set paused to true
      timer.stop(); //Stop the timer
      stopTime = System.currentTimeMillis(); //Get the time the timer stopped
      gv.pause(); //Display the pause screen
      gv.repaint(); //Repaint the screen
      self.requestFocus(); //Set focus to the playGame object
    }
  }

  /**
   * Resumes the timer.
  */
  public void resume() {
    if (Playgame.paused && !Game.isGameOver()) {
      timer.start();
      startTime = startTime + (System.currentTimeMillis() - stopTime);
      self.requestFocus();
      gv.unpause();
      Playgame.paused = false;
      gv.play();
    }

  }
  public void restart(){
    timer.restart();
    startTime = System.currentTimeMillis();
    self.requestFocus();
    gv.play();
  }
}
