package nz.ac.vuw.ecs.swen225.gp22.recorder;

import nz.ac.vuw.ecs.swen225.gp22.domain.*;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Utility class to the Recorder module which controls
 * the saving of a Game to an XML file functionality.
 *
 * @author Finn McKeefry.
 */
public class ParseToXml {

    /**
     * Parses a Game to a Xml file.
     * This method is protected as it should
     * only be called via the Recorder class.
     *
     * @param game   The Game to be parsed to a Xml file.
     * @param toSave The Xml file to save the parsed Game to.
     * @return True if the parsing and saving of the Game was successful, false otherwise.
     */
    protected static boolean toXml(Game game, File toSave) {
        if (game == null || toSave == null){
            return false;
        }
        Element rootGameElement = new Element("Game"); //Craft a root element to save to a file.
        for (Level level : Game.levels()) {
            Element levelElement = new Element("Level");
            levelElement.setAttribute("number", level.getLevelNumber() + "");
            levelElement.addContent(saveActor("Player", level.player())); //Save player.
            if (level.enemies() != null) {
                for (Actor enemy : level.enemies()) { //Save these levels' enemies.
                    levelElement.addContent(saveActor("Enemy", enemy)); //Save enemies (must be in correct order).
                }
            }
            rootGameElement.addContent(levelElement); //Add each level element to the root element.
        }
        return saveGameToFile(rootGameElement, toSave);
    }

    //======================================================================================//
    //                                    Helper methods                                    //
    //======================================================================================//

    /**
     * Helper save method for toXml.
     *
     * @param type  A String which corresponds to a type of Actor (player or enemy).
     * @param actor The actorElement's corresponding Actor which provides the undo-stack in order to parse the Actor's action history to a Xml file.
     * @return The Element to represent actor in a xml file.
     */
    private static Element saveActor(String type, Actor actor) {
        Element actorElement = new Element(type);
        actor.getUndoStack().forEach(a -> actorElement.addContent(saveAction(a))); //Make sure to use undoStack not redoStack.
        return actorElement;
    }

    /**
     * Helper save method for saveActor.
     *
     * @param action The Action to be translated into an Element which represents an Action.
     * @return The translated Element.
     */
    private static Element saveAction(Action action) {
        Element actionElement = new Element("Action");
        actionElement.setAttribute("ping", action.pingCount() + "");
        actionElement.addContent(new Element("EndDirection").setText(stringFromDirection(action.getEndDirection())));
        return actionElement;
    }

    /**
     * Helper method for saveAction which returns a specific
     * Direction enum based on the String 'direction'.
     * up    -> Direction.UP
     * down  -> Direction.DOWN
     * left  -> Direction.LEFT
     * right -> Direction.RIGHT
     *
     * @param direction The String which determines the Direction to return.
     * @return The correct Direction based on 'direction'.
     */
    public static String stringFromDirection(Direction direction) {
        if (direction == null) {
            return "";
        }
        return switch (direction) {
            case UP -> "up";
            case DOWN -> "down";
            case LEFT -> "left";
            case RIGHT -> "right";
        };
    }

    /**
     * Helper save method for saveGame.
     * Saves the pre-parsed 'rootGameElement' to a xml file.
     *
     * @param rootGameElement The root element that represents the parsed game.
     * @param toSave          The file to save the recorded game to.
     * @return True if the root element was parsed to a xml file successfully, false otherwise.
     */
    private static boolean saveGameToFile(Element rootGameElement, File toSave) {
        try (FileOutputStream fos = new FileOutputStream(toSave)) {
            Document document = new Document(rootGameElement);
            XMLOutputter xmlOutputter = new XMLOutputter();
            xmlOutputter.setFormat(Format.getPrettyFormat());
            xmlOutputter.output(document, fos);
            return true; //File saving successful.
        } catch (IOException e) {
            return false; //File saving unsuccessful.
        }
    }
}
