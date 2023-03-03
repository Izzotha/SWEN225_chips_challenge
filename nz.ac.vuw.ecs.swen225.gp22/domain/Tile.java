package nz.ac.vuw.ecs.swen225.gp22.domain;

/**
 *
 */
public class Tile {

    Position position;
    TileType type;

    public Tile(Position position, TileType type) {
        this.position = position;
        this.type = type;
    }

    public Tile(Position position, char c) {
        this.position = position;
        this.type = fromChar(c);
    }

    public Position position() {
        return position;
    }

    public static TileType fromChar(char c) {
        return switch (c) {
            case '#' -> new Tile.wall();
            case ' ', 'P' -> new Tile.free();
            case 'R' -> new Tile.lockedDoor(Colour.RED);
            case 'G' -> new Tile.lockedDoor(Colour.GREEN);
            case 'B' -> new Tile.lockedDoor(Colour.BLUE);
            case 'Y' -> new Tile.lockedDoor(Colour.YELLOW);
            case 'r' -> new Tile.key(Colour.RED);
            case 'g' -> new Tile.key(Colour.GREEN);
            case 'b' -> new Tile.key(Colour.BLUE);
            case 'y' -> new Tile.key(Colour.YELLOW);
            case '$' -> new Tile.treasure();
            case '?' -> new Tile.info();
            case 'X' -> new Tile.exit();
            case '%' -> new Tile.exitLock();
            case '@' -> new Teleport();
            case '&' -> new Tile.slime();
            default -> throw new IllegalArgumentException("Invalid tile character: " + c);
        };
    }

    public boolean isWalkable() {
        return type.isWalkable();
    }

    public void undoInteract(Action action) {
        type.undoInteract(this, action);
    }

    public ActionType interact(Action action) {
        return type.interact(this, action);
    }

    public String toString() {
        return type.toString();
    }

    public String name() {
        return type.name();
    }

    public TileType type() {
        return type;
    }

    public boolean onPing(Actor actor) {
        return type.onPing(this, actor);
    }


    static class free implements TileType {
        @Override
        public boolean isWalkable() {
            return true;
        }

        @Override
        public String toString() {
            return " ";
        }

        @Override
        public String name() {
            return "FREE";
        }
    }

    record wall() implements TileType {

        @Override
        public ActionType interact(Tile self, Action action) {
            return ActionType.WALLBUMP;
        }

        @Override
        public String toString() {
            return "#";
        }

        @Override
        public String name() {
            return "WALL";
        }
    }

    record key(Colour colour) implements TileType {

        @Override
        public boolean isWalkable() {
            return true;
        }

        @Override
        public ActionType interact(Tile self, Action action) {
            action.getActor().addToInventory(this);
            self.type = new Tile.free(){
                @Override
                public boolean undoInteract(Tile self, Action action) {
                    action.getActor().removeFromInventory(new key(colour));
                    self.type = new Tile.key(colour);
                    return true;
                }
            };

            return ActionType.COLLECT;
        }

        @Override
        public String toString() {
            return switch(colour) {
                case RED -> "r";
                case BLUE -> "b";
                case GREEN -> "g";
                case YELLOW -> "y";
            };
        }

        @Override
        public String name() {
            return colour.name() + "_KEY";
        }
    }

    record lockedDoor(Colour colour) implements TileType {

        @Override
        public ActionType interact(Tile self, Action action) {
            if(action.getActor() instanceof Enemy) return ActionType.NONE;

            if(action.getActor().removeFromInventory(new Tile.key(colour))){
                self.type = new Tile.free(){
                    @Override
                    public boolean undoInteract(Tile self, Action action) {
                        action.getActor().addToInventory(new Tile.key(colour));
                        self.type = new Tile.lockedDoor(colour);
                        return true;
                    }
                };
                return ActionType.UNLOCK;
            }

            return ActionType.WALLBUMP; // Did not have the key
        }

        @Override
        public String toString() {
            return switch (colour) {
                case RED -> "R";
                case BLUE -> "B";
                case GREEN -> "G";
                case YELLOW -> "Y";
            };
        }

        @Override
        public String name() {
            return colour().name() + "_DOOR";
        }
    }


    record info() implements TileType {

        @Override
        public ActionType interact(Tile self, Action action) {
            return ActionType.NOTIF;
        }

        @Override
        public boolean isWalkable() {
            return true;
        }

        @Override
        public String toString() {
            return "?";
        }

        @Override
        public String name() {
            return "INFO";
        }
    }

    record treasure() implements TileType {

        @Override
        public ActionType interact(Tile self, Action action) {
            if(action.getActor() instanceof Enemy) return ActionType.NONE;

            self.type = new Tile.free(){
                @Override
                public boolean undoInteract(Tile self, Action action) {
                    self.type = new Tile.treasure();
                    return true;
                }
            };

            return ActionType.COLLECT;
        }

        @Override
        public String toString() {
            return "$";
        }

        @Override
        public String name() {
            return "TREASURE";
        }
    }

    record exitLock() implements TileType {
        @Override
        public ActionType interact(Tile self, Action action) {
            if (action.getActor() instanceof Enemy) return ActionType.NONE;

            if(Game.currentLevel().treasureCount() == 0){
                self.type = new free(){
                    @Override
                    public boolean undoInteract(Tile self, Action action) {
                        self.type = new exitLock();
                        return true;
                    }
                };
                return ActionType.UNLOCK;
            }
            return ActionType.NONE;
        }

        @Override
        public String toString() {
            return "%";
        }

        @Override
        public String name() {
            return "EXIT_LOCK";
        }
    }

    record exit() implements TileType {
        @Override
        public boolean isWalkable() {
            return true;
        }

        @Override
        public String toString() {
            return "X";
        }

        @Override
        public String name() {
            return "EXIT";
        }

    }

    static class slime implements TileType {

        @Override
        public ActionType interact(Tile self, Action action) {
            return ActionType.SLIME;
        }

        @Override
        public boolean isWalkable() {
            return true;
        }

        @Override
        public String toString() {
            return "&";
        }

        @Override
        public String name() {
            return "SLIME";
        }
    }

    enum Colour{
        RED, GREEN, BLUE, YELLOW

    }
}




