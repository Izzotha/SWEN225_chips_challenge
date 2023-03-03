package nz.ac.vuw.ecs.swen225.gp22.recorder;

import nz.ac.vuw.ecs.swen225.gp22.domain.Game;

import java.io.File;

/**
 * The main class of the recorder module which controls
 * the parsing utility classes as well as handling the
 * interactions between the recorder and app modules.
 *
 * @author Finn McKeefry.
 */
public class Recorder {

    /**
     * This method uses ParseToXml to save 'game' to a xml file.
     *
     * @param game The 'game' object to be parsed into a xml file for later replaying of games.
     * @return True if the game was saved successfully to a xml file, false otherwise.
     */
    public static boolean saveGame(Game game, File toSave) {
        return ParseToXml.toXml(game, toSave);
    }

    /**
     * This method uses ParseFromXml to reload a previously recorded game.
     *
     * @param xmlFile The file to load the game from.
     * @return The reloaded game.
     */
    public static Game loadGame(File xmlFile) {
        return ParseFromXml.fromXml(xmlFile);
    }
}
