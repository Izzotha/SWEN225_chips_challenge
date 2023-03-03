package nz.ac.vuw.ecs.swen225.gp22.recorder;

import nz.ac.vuw.ecs.swen225.gp22.domain.*;
import nz.ac.vuw.ecs.swen225.gp22.persistency.Persistence;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaderJDOMFactory;
import org.jdom2.input.sax.XMLReaderXSDFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Utility class for the Recorder module.
 * Parses an XML file to a Game object which can then
 * be used to step through said Game.
 *
 * @author Finn McKeefry.
 */
public class ParseFromXml {

    /**
     * Parses an XML file of a saved game into a Game object.
     * This method is protected as it should only be called
     * via the Recorder class.
     *
     * @param file A xml file which contains information about the game to be loaded.
     * @return The loaded Game.
     */
    protected static Game fromXml(File file) {
        try {
            File xsd = new File("./XSD/RecorderSchema.xsd");
            XMLReaderJDOMFactory xmlReader = new XMLReaderXSDFactory(xsd);
            SAXBuilder builder = new SAXBuilder(xmlReader); //Ensure the file used adheres to the correct xml schema.
            Document doc = builder.build(file);
            Element rootElement = doc.getRootElement();
            Game g =  new Game(loadLevels(rootElement).toArray(Level[]::new));
            System.out.println(g);
            return new Game(loadLevels(rootElement).toArray(Level[]::new));
        } catch (JDOMException | IOException e) {
            System.out.println("Error parsing XML file.");
            return  null;
        }
    }

    //======================================================================================//
    //                                    Helper methods                                    //
    //======================================================================================//

    /**
     * Helper method for fromXml.
     *
     * @param gameElement The Element to be parsed into a Game object.
     * @return The fully-parsed Game object.
     */
    private static List<Level> loadLevels(Element gameElement) {
        List<Level> gameLevels = new ArrayList<>();
        gameElement.getChildren().forEach(c -> gameLevels.add(loadLevel(c)));
        return gameLevels;
    }

    /**
     * Helper method for loadLevels.
     *
     * @param levelElement The Element to be parsed into a Level object.
     * @return The fully-parsed Level.
     */
    private static Level loadLevel(Element levelElement) {
        String levelNumber = levelElement.getAttributeValue("number");
        Level level = levelNumber.equals("1") ? Persistence.loadLevel("./Levels/level1.xml") : Persistence.loadLevel("./Levels/level2.xml"); //Determine the current Level being loaded.
        for (int i = 0; i < levelElement.getChildren().size(); i++) { //Load all Actors belonging to the current Level being loaded.
            loadActor(level, levelElement.getChildren().get(i), i);
        }
        return level;
    }

    /**
     * Helper method for loadLevel.
     *
     * @param level        The level which contains the Actors for which their redo-stacks should be set.
     * @param actorElement The Element used to determine which Actor should have a redo-stack set via parsing.
     */
    private static void loadActor(Level level, Element actorElement, int i) {
        if (i == 0) { //i==0 represents the Player Element of the Xml file being parsed.
            //Player must be the first Element of each Level Element.
            level.player().setRedoStack(loadRedoStack(actorElement, level.player())); //Load the player.
        } else { //i>0 represents a specific Enemy Element of the Xml file being parsed.
            Actor enemy = level.enemies()[i]; //Enemies must come after Players in Xml file.
            enemy.setRedoStack(loadRedoStack(actorElement, enemy)); //Load the enemies.
        }
    }

    /**
     * Helper method for loadActor.
     *
     * @param actorElement The Actor which the Stack of Action's to be returned belongs to.
     * @param actor        Used in future helper method for creating actions that belong to it.
     * @return Essentially, the redo-stack of an Actor's actions.
     */
    private static Stack<Action> loadRedoStack(Element actorElement, Actor actor) {
        Stack<Action> actions = new Stack<>();
        actorElement.getChildren().forEach(c -> actions.push(loadAction(c, actor)));
        return actions;
    }

    /**
     * Helper method for loadRedoStack.
     *
     * @param actionElement The Element used to make the Action to be returned.
     * @param actor         The Actor the Action to be returned is concerned with.
     * @return An Action, based off of actionElement and actor.
     */
    private static Action loadAction(Element actionElement, Actor actor) {
        Direction endDirection = directionFromString(actionElement.getChildText("EndDirection"));
        int pingOffset = Integer.parseInt(actionElement.getAttributeValue("ping"));
        return new Action(actor, endDirection, pingOffset);
    }

    /**
     * Helper method for loadAction.
     *
     * @param directionType A string to be translated into its corresponding Direction enum.
     * @return The Direction enum matching the directionType parameter.
     */
    public static Direction directionFromString(String directionType) {
        if (directionType == null) {
            return null;
        }
        return switch (directionType) {
            case "up" -> Direction.UP;
            case "down" -> Direction.DOWN;
            case "left" -> Direction.LEFT;
            case "right" -> Direction.RIGHT;
            default ->
                    throw new IllegalArgumentException("Invalid Direction-type String. "); //Dead code due to xml validation against RecorderSchema.
        };
    }
}