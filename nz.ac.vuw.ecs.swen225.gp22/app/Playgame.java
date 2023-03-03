package nz.ac.vuw.ecs.swen225.gp22.app;

import static nz.ac.vuw.ecs.swen225.gp22.persistency.Persistence.loadLevel;
import static nz.ac.vuw.ecs.swen225.gp22.persistency.Persistence.saveLevel;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import javax.swing.*;
import nz.ac.vuw.ecs.swen225.gp22.domain.Game;
import nz.ac.vuw.ecs.swen225.gp22.domain.Level;
import nz.ac.vuw.ecs.swen225.gp22.recorder.Recorder;
import nz.ac.vuw.ecs.swen225.gp22.renderer.GameView;
import nz.ac.vuw.ecs.swen225.gp22.renderer.Image;
import nz.ac.vuw.ecs.swen225.gp22.renderer.Sound;


/**
 * Playgame.
 *
 * <p> Runs and Displays the Game
 * --Needs to be updated to handle game inputs </p>
 *
 * @author Luke Juriss
 */
public class Playgame extends JFrame {
  /**
   * This current game being displayed in gameView
   */
  static Game game;
  static Game recorderGame;

  /**
   * The countdown timer for the game
   * also handles screen updates and minor game logic
   */
  static Countdown c;

  /**
   * The music for the game
   */
  Sound music;
  /**
   *
   */
  static boolean paused; //Whether the game is paused or not
  private boolean help; //Whether the user wants to display help text or not
  static GameView gv; //The gameView that displays the game
  static JLabel keysCollected = new JLabel(" Keys Collected: ");
  //The label that displays the number of keys collected
  static JLabel treasureRemaining = new JLabel(" Bananas Remaining: ");
  //The label that displays the number of bananas remaining
  static JLabel levelNum = new JLabel(" Level: ");
  //The label that displays the current level number
  static JLabel clock = new JLabel(" Time Remaining: ");
  //The label that displays the time remaining
  static JButton pauseButton = new JButton("Pause");
  //The button that pauses and resumes the game
  static JButton exitButton = new JButton("Exit Game");
  //The button that exit the game
  static JButton SaveButton = new JButton("Save Game");
  //The button that saves the game
  static JButton loadButton = new JButton("Load Game");
  //The button that loads and resumes a saved game
  static JButton helpButton = new JButton("Open Help");
  //The button that loads and resumes a saved game

  static JButton recorderButton = new JButton("Load Recorded Game");
  //The button that loads a recorded game

  static JButton undoButton = new JButton("undo");
  //The button undoes an action in a recorded game
  static JButton redoButton = new JButton("redo");
  //The button redoes an undone action in a recorded game
  private JTextArea helpText = new JTextArea("" +
                                          "CTRL-X - exit the game without saving.\n" +
                                          "CTRL-S - exit the game, saves the game state.\n" +
                                          "CTRL-R - resume a saved game.\n" +
                                          "CTRL-1 - start a new game at level 1.\n" +
                                          "CTRL-2 - start a new game at level 2.\n" +
                                          "SPACE - pause the game.\n" +
                                          "ESC - resume the game.\n" +
                                          "UP, DOWN, LEFT, RIGHT ARROWS\n" +
                                          "move Chap within the maze.");


  /**
   * Constructor for the playGame class.
   */
  public Playgame() {
    assert SwingUtilities.isEventDispatchThread();
    this.setPreferredSize(new Dimension(1050, 600)); //set the size of the window
    homeScreen(); //display the home screen
    this.setVisible(true);
    this.pack();
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    music = new Sound();
    addWindowListener(new WindowAdapter() {
      //add a window listener to stop the music when the window is closed
      @Override
      public void windowClosing(java.awt.event.WindowEvent windowEvent) {
        music.stop();   //stop music when window is closed
        System.exit(1);
      }
    });

  }

  /**
   * Displays the home screen.
   */
  public void homeScreen() { //Displays the homescreen on startup
    if (this.getKeyListeners().length > 0) { //reset the key listener
      this.removeKeyListener(this.getKeyListeners()[0]);
    }
    this.remove(this.getContentPane()); //remove the current content pane
    this.add(new JLabel(new ImageIcon(Image.getTitleScreenImage()))); //add the title screen image
    this.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        //if the user presses spaceBar, start the game
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
          Level save = loadLevel("./SavedLevels/LatestSave.xml");
          if (save == null) { //if there is no save file, start a new game
            displayGame(new Game(loadLevel("./Levels/level1.xml")));
          } else {
            displayGame(new Game(save));
          }

        }
      }
    });

  }

  /**
   * Creates a JFrame for the game which includes the GameView panel and stats panel.
   * Displays the game and starts the countdown timer.
   *
   * @param currentGame the game to be displayed
   */
  void displayGame(Game currentGame) {
    this.removeKeyListener(this.getKeyListeners()[0]); //remove the key listener from the homescreen
    this.remove(this.getContentPane().getComponents()[0]); //remove the homescreen image
    game = currentGame; //set the game to the current game
    music.playBgm(); //play and loop the background music
    gv = new GameView(game); //create a new gameView for this game
    //Create the two panels
    final JPanel statsPanel = new JPanel(); //the stats panel

    //All Button functionality
    pauseButton.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        //Give the pause button functionality
        if (!paused) {
          pauseButton.setText("Resume");
          c.pause();
        } else {
          pauseButton.setText("Pause");
          c.resume();
        }
      }
    });

    exitButton.addMouseListener(new java.awt.event.MouseAdapter() {
      //Give the exit button functionality
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        music.stop();
        System.exit(1);
      }
    });

    SaveButton.addMouseListener(new java.awt.event.MouseAdapter() {
      //Give the save button functionality
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        JFileChooser fileChooser = new JFileChooser("./SavedLevels/");
        fileChooser.setDialogTitle("Set A filename to Save");
        int userSelection = fileChooser.showSaveDialog(gv);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
          System.out.println("Save as file: " + fileChooser.getSelectedFile().getName());
          saveLevel(fileChooser.getSelectedFile().getAbsolutePath(), Game.currentLevel());
        }
        System.exit(1);
      }
    });

    loadButton.addMouseListener(new java.awt.event.MouseAdapter() {
      //Give the load button functionality
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        JFileChooser fileChooser = new JFileChooser("./SavedLevels/");
        fileChooser.setDialogTitle("Select a level to load");
        int userSelection = fileChooser.showOpenDialog(gv);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
          loadLevel(String.valueOf(fileChooser.getSelectedFile()));
          c.restart();
        }
       c.resume();
      }
    });
    //Give the help button functionality
    helpButton.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        // boolean help determines whether the help tab is open already
        if (!help) {
          helpButton.setText("Close Help");
          helpText.setForeground(Color.WHITE);
          helpText.setFont(new Font("Monospaced", Font.BOLD, 12));
          helpText.setMaximumSize(new Dimension(300,200));
          helpText.setBackground(Color.darkGray.darker().darker());
          statsPanel.add(helpText);
          help = true;

        }
        else { helpButton.setText("Open Help");
          statsPanel.remove(helpText);
          help = false;
        }
        statsPanel.repaint();
        focus();
      }
    });

    recorderButton.addMouseListener(new java.awt.event.MouseAdapter() {
      //Give the recorder button functionality
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        JFileChooser fileChooser = new JFileChooser("./RecordedGames/");
        fileChooser.setDialogTitle("Select a level to load");
        int userSelection = fileChooser.showOpenDialog(gv);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
          Game recordGame = Recorder.loadGame(fileChooser.getSelectedFile());
          setRecorderGame(recordGame,statsPanel);
        }



      }
    });


    //End of button functionality
    gv.setLayout(new BoxLayout(gv, BoxLayout.Y_AXIS));
    //Add the content to the stats panel
    statsPanel.setBackground(Color.darkGray.darker().darker());
    statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
    statsPanel.setPreferredSize(new Dimension(200, 580));
    statsPanel.add(new JLabel(" "));
    statsPanel.add(clock);
    statsPanel.add(new JLabel(" "));
    statsPanel.add(levelNum);
    statsPanel.add(new JLabel(" "));
    statsPanel.add(keysCollected);
    statsPanel.add(new JLabel(" "));
    statsPanel.add(treasureRemaining);
    statsPanel.add(new JLabel("  "));
    statsPanel.add(pauseButton);
    statsPanel.add(new JLabel("  "));
    statsPanel.add(exitButton);
    statsPanel.add(new JLabel("  "));
    statsPanel.add(SaveButton);
    statsPanel.add(new JLabel("  "));
    statsPanel.add(loadButton);
    statsPanel.add(new JLabel("  "));
    statsPanel.add(recorderButton);
    statsPanel.add(new JLabel("  "));
    statsPanel.add(helpButton);

    for(Component c: statsPanel.getComponents()){  //set the theme color for all the components in the stats panel
      c.setBackground(Color.darkGray.darker().darker());
      c.setForeground(Color.yellow);
      c.setFont(new Font("Monospaced", Font.BOLD, 16));
    }

    //repaint Jpanels
    statsPanel.repaint();
    gv.repaint();

    //Create new countdown timer and start it
    c = new Countdown(this, gv, 60000);
    c.start();
    this.addKeyListener(new Controller(gv));
    this.setFocusable(true);
    focus();


    //Add the game panel to the frame
    this.add(gv, BorderLayout.WEST);
    this.add(statsPanel, BorderLayout.CENTER);
  }

  /**
   * Load a specific level.
   *
   * @param levelNum the level number to load
   */
  public static void loadLevelNum(int levelNum) {
    if (levelNum == 1) {
      game = new Game(loadLevel("./Levels/level1.xml"));

    } else {
      game = new Game(loadLevel("./Levels/level2.xml"));

    }
    c.restart();

  }
  public void focus(){
    this.requestFocus();
  }

  public void setRecorderGame(Game recordedGame,JPanel statsPanel){

    gv = new GameView(recordedGame);
    undoButton.addMouseListener(new java.awt.event.MouseAdapter() {
      //Give the load button functionality
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        if (recorderGame != null) {
          System.out.println("Undo");
          recorderGame.undo();
          gv.repaint();
        }
      }
    });
    redoButton.addMouseListener(new java.awt.event.MouseAdapter() {
      //Give the load button functionality
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        if (recorderGame != null) {
          System.out.println("Redo");
          recorderGame.redo();
          gv.repaint();
        }
      }
    });
    statsPanel.add(undoButton);
    statsPanel.add(redoButton);
  }
}


