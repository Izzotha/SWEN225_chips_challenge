package nz.ac.vuw.ecs.swen225.gp22.domain;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A level in the game.
 *
 * Holds the tiles and actors in the level
 * Also holds all the enemies
 *
 * Author: Ninos Awas
 */
public class Level {
    private int levelNumber;
    private Actor player;
    private Actor[] enemies;
    private Tile[][] tiles;
    private int columns;
    private int rows;

    public Level(int lvlNumber, Actor player, Tile[][] tiles) {
        this.levelNumber = lvlNumber;
        this.player = player;
        this.enemies = new Actor[0];
        this.rows = tiles.length;
        this.columns = tiles[0].length;
        this.tiles = tiles;
    }

    public Level(int lvlNumber, Actor player, Actor[] enemies, Tile[][] tiles) {
        this.levelNumber = lvlNumber;
        this.player = player;
        this.enemies = enemies;
        this.tiles = tiles;
    }

    /**
     * Generates a level from a string.
     * @return
     */
    public Level(int lvlNumber, String... level) {
        this.levelNumber = lvlNumber;
        this.rows = level.length;
        this.columns = level[0].length();
        this.enemies = new Actor[0];
        this.tiles = new Tile[rows][columns];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                if (level[row].charAt(col) == 'P') {
                    this.player = new Actor(col, row);
                    this.tiles[row][col] = new Tile(new Position(col, row), new Tile.free());
                }else if(level[row].charAt(col) == 'E'){
                    this.enemies = Stream.concat(Arrays.stream(enemies),
                            Stream.of(new Enemy(col, row))).toArray(Actor[]::new);
                    this.tiles[row][col] = new Tile(new Position(col, row), new Tile.free());
                }else {
                    this.tiles[row][col] = new Tile(new Position(col, row), level[row].charAt(col));
                }
            }
        }
    }

    public static Level level1() {
        return new Level(1,
            "  ##### #####  ",
            "  #   ###   #  ",
            "  # $ #X# $ #  ",
            "#####G#%#G#####",
            "# y B     R y #",
            "# $ #b ? r# $ #",
            "#####$ P $#####",
            "# $ #b   r# $ #",
            "#   R  $  B   #",
            "######Y#Y######",
            "    #  #  #    ",
            "    # $#$ #    ",
            "    # g#g #    ",
            "    #######    "
        );
    }

    public void initTeleporters(Map<Position, Position> teleporters) {
        for (Map.Entry<Position, Position> pair : teleporters.entrySet()) {
            Position teleporter = pair.getKey();
            Position target = pair.getValue();
            Tile teleporterTile = tiles[(int) teleporter.y()][(int) teleporter.x()];
            if(!(teleporterTile.type instanceof Teleport tp))
                throw new IllegalArgumentException("Teleporter tile not found at " + teleporter);

            tp.setTarget(target);
        }
    }

    public static Level level2() {
        Level lvl = new Level(2,
            "         #############       ",
                    "     #### $ $ # $ $ ###      ",
                    "     #  gE    #r    $  ##    ",
                    "     #######  #   $ E $  #   ",
                    "     #        #     $    #   ",
                    " #####       &&###       #   ",
                    " #$$y       &   %        #   ",
                    " #G#####@###& X #####Y####   ",
                    " #     #$   &   &      #$#   ",
                    " #  $b$#$    &&&     # #E#   ",
                    " #######      #      #   #   ",
                    " #   $ #      R $ ####B###   ",
                    " #$$#    P    # $ # @ $  #   ",
                    " #########################   "

        );
        // tp mid-left x=8 y=7  target x=15 y=10
        // tp bot-right x=20 y=12
        lvl.initTeleporters(Map.of(
                new Position(8, 7), new Position(20, 12),
                new Position(20, 12), new Position(8, 7)
        ));
        return lvl;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public Actor player() {
        return player;
    }

    public Tile getTile(Position target) {
        return tiles[(int) target.y()][(int) target.x()];
    }

    @Override
    public String toString() {
        return Stream.of(tiles)
                .map(row -> Stream.of(row)
                    .map(tile ->{
                        if(tile.position().equals(player.position())) {
                            return "P";
                        } else if (Arrays.stream(enemies()).anyMatch(enemy -> enemy.position().equals(tile.position()))) {
                            return "E";
                        } else {
                            return tile.toString();
                        }
                    })
                    .collect(Collectors.joining()))
                .collect(Collectors.joining("\n"));

    }

    public Actor[] enemies() {
        return enemies;
    }

    public Level[] getLevels() {
        return new Level[] {level1()};
    }

    public int  treasureCount() {
        return (int) Stream.of(tiles)
                .flatMap(Stream::of)
                .filter(tile -> tile.type() instanceof Tile.treasure)
                .count();
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public int width() {
       return getTiles()[0].length;
    }

    public int height() {
        return getTiles().length;
    }
}
