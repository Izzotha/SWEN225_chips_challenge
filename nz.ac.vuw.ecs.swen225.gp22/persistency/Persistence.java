package nz.ac.vuw.ecs.swen225.gp22.persistency;

import nz.ac.vuw.ecs.swen225.gp22.domain.*;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaderJDOMFactory;
import org.jdom2.input.sax.XMLReaderXSDFactory;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Persistence {
  //Load level from file
  public static Level loadLevel(String filename) {
    try {
      File file = new File(filename);
      if(!file.exists()) return null;

      File xsd = new File("./XSD/level.xsd");
      XMLReaderJDOMFactory xmlReader = new XMLReaderXSDFactory(xsd);
      SAXBuilder builder = new SAXBuilder(xmlReader); //Ensure the file used adheres to the correct xml schema.
      Document doc = builder.build(file);
      Element root = doc.getRootElement();
      int rows = Integer.parseInt(root.getAttributeValue("height"));
      int cols = Integer.parseInt(root.getAttributeValue("width"));
      int levelNum = Integer.parseInt(root.getAttributeValue("number"));
      Tile[][] tiles = new Tile[rows][cols];
      Actor player = null;
      List<Actor> enemies = new ArrayList<>();
      Map<Position, Position> teleporterData = new HashMap<>();

      // Load the tiles
      for (Element tileRow : root.getChildren("TileRow")) {
        for (Element tile : tileRow.getChildren()) {
          int x = (int) Double.parseDouble(tile.getAttributeValue("x"));
          int y = (int) Double.parseDouble(tile.getAttributeValue("y"));
          char c = tile.getAttributeValue("char").charAt(0);

          tiles[y][x] = new Tile(new Position(x, y), c);
        }
      }

      // Load the player
      Element playerElement = root.getChild("Player");
      Direction dir = Direction.valueOf(playerElement.getAttributeValue("dir"));
      int x = (int) Double.parseDouble(playerElement.getAttributeValue("x"));
      int y = (int) Double.parseDouble(playerElement.getAttributeValue("y"));
      player = new Actor(x, y);
      player.setDirection(dir);


      // Load the enemies
      for (Element enemy : root.getChild("EnemyData").getChildren()) {
        x = (int) Double.parseDouble(enemy.getAttributeValue("x"));
        y = (int) Double.parseDouble(enemy.getAttributeValue("y"));
        enemies.add(new Enemy(x, y));
      }

      // Load the player inventory
      Element inventoryElement = playerElement.getChild("Inventory");
      for (Element itemElement : inventoryElement.getChildren("Item")) {
        String itemName = itemElement.getAttributeValue("char");
        player.addToInventory(Tile.fromChar(itemName.charAt(0)));
      }

      Level level = new Level(levelNum, player, enemies.toArray(Actor[]::new), tiles);

      //Load Teleport data into level
      for (Element teleport : root.getChild("TeleportData").getChildren()) {
        int x1 = (int) Double.parseDouble(teleport.getAttributeValue("x1"));
        int y1 = (int) Double.parseDouble(teleport.getAttributeValue("y1"));
        int x2 = (int) Double.parseDouble(teleport.getAttributeValue("x2"));
        int y2 = (int) Double.parseDouble(teleport.getAttributeValue("y2"));
        Position pos1 = new Position(x1, y1);
        Position pos2 = new Position(x2, y2);
        teleporterData.put(pos1, pos2);
      }

      level.initTeleporters(teleporterData);
      return level;

    } catch (JDOMException | IOException e) {
      e.printStackTrace();
      return null;
    }
  }


  public static boolean saveLevel(String filename, Level lvl) {
    Tile[][] tiles = lvl.getTiles();
    Element root = new Element("level");
    root.setAttribute("width", lvl.width() + "");
    root.setAttribute("height", lvl.height() + "");
    root.setAttribute("number", lvl.getLevelNumber() + "");
    Document doc = new Document(root);
    try {
      FileOutputStream fos = new FileOutputStream("./" + filename);
      XMLOutputter XMLout = new XMLOutputter(Format.getPrettyFormat());
      Element teleportData = new Element("TeleportData");
      for (int row = 0; row < tiles.length; row++) {
        Element tileRow = new Element("TileRow");
        tileRow.setAttribute("row_number", row + "");
        for (int col = 0; col < tiles[row].length; col++) {
          Element tile = new Element("tile");
          Tile currentTile = tiles[row][col];
          if (currentTile.type() instanceof Teleport tp) {
            teleportData.addContent(getTeleportElement(currentTile, tp));
          }
          tile.setAttribute("x", currentTile.position().x() + "");
          tile.setAttribute("y", currentTile.position().y() + "");
          tile.setAttribute("char", currentTile.toString() + " ");
          tileRow.addContent(tile);
        }
        root.addContent(tileRow);
      }
      teleportData.setAttribute("size", teleportData.getContentSize() + "");
      root.addContent(teleportData);
      root.addContent(getPlayerElement(lvl.player()));
      root.addContent(getEnemyElement(lvl.enemies()));

      System.out.println("Done saving");
      XMLout.output(doc, fos);
      fos.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return true;
  }

  private static Element getEnemyElement(Actor[] enemies) {
    Element enemyData = new Element("EnemyData");
    for (Actor enemy : enemies) {
      Element enemyElement = new Element("Enemy");
      enemyElement.setAttribute("x", enemy.position().x() + "");
      enemyElement.setAttribute("y", enemy.position().y() + "");
      enemyData.addContent(enemyElement);
    }
    return enemyData;
  }

  private static Element getPlayerElement(Actor player) {
    Element playerElement = new Element("Player");
    playerElement.setAttribute("dir", player.direction().toString());
    playerElement.setAttribute("x", player.position().x() + "");
    playerElement.setAttribute("y", player.position().y() + "");

    Element inventory = new Element("Inventory");
    for (TileType item : player.inventory()) {
      Element itemElement = new Element("Item");
      itemElement.setAttribute("char", item.toString());
      inventory.addContent(itemElement);
    }
    playerElement.addContent(inventory);
    return playerElement;
  }

  private static Element getTeleportElement(Tile currentTile, Teleport tp) {
    Element teleport = new Element("teleport");

    teleport.setAttribute("x1", currentTile.position().x() + "");
    teleport.setAttribute("y1", currentTile.position().y() + "");

    teleport.setAttribute("x2", tp.target().x() + "");
    teleport.setAttribute("y2", tp.target().y() + "");

    return teleport;
  }


}