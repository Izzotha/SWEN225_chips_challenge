package nz.ac.vuw.ecs.swen225.gp22.domain;

import java.util.*;

/**
 * An actor in the game.
 *
 * @author Ninos Awas
 */
public class Actor {

    private Position position;

    private Direction direction = Direction.DOWN;

    private Action currentAction = null;

    private Stack<Action> actionHistory = new Stack<>();
    private Stack<Action> undoHistory = new Stack<>();

    private List<TileType> inventory = new ArrayList<>();

    private ActionObserver observer = null;

    public Actor(double x, double y) {
        this.position = new Position(x, y);
    }

    public double x() {
        return position.x();
    }

    public double y() {
        return position.y();
    }

    public void move(Direction d){
        if(currentAction != null) throw new IllegalStateException("Actor is already performing an action");
        currentAction = new Action(this, d);
    }

    public void undo(){
        if(actionHistory.isEmpty()) return;
        Action action = actionHistory.pop();
        action.undo();
        undoHistory.push(action);
    }

    public void redo(){
        if(undoHistory.isEmpty()) return;
        Action action = undoHistory.pop();
        action.apply();
        actionHistory.push(action);
    }

    public void ping(){
        if(currentAction == null) return;

        currentAction.apply();
        actionHistory.push(currentAction);
        undoHistory.clear();
        currentAction = null;
    }

    public void addToInventory(TileType type) {
        if(type == null) throw new IllegalArgumentException("Null TileType cannot be added to inventory");
        inventory.add(type);
    }

    public boolean removeFromInventory(TileType type) {
        if(type == null) throw new IllegalArgumentException("Null tile cannot be removed from inventory");
        return inventory.remove(type);
    }

    public boolean hasMoved() {
        return !actionHistory.isEmpty() && actionHistory.peek().hasMoved()
            && actionHistory.peek().pingCount() == Game.PING_COUNT-1;
    }

    public List<TileType> inventory() {
        return Collections.unmodifiableList(inventory);
    }

    public Direction direction() {
        return direction;
    }

    public Position position() {
        return position;
    } 
    public void setPosition(Position position) {
        this.position = position;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Collection<Action> getUndoStack() {
        return Collections.unmodifiableCollection(actionHistory);
    }

    public void setRedoStack(Stack<Action> undoHistory) {
        this.undoHistory = undoHistory;
    }

    public void attach(ActionObserver observer) {
        this.observer = observer;
    }

    public ActionObserver getObserver() {
        return observer;
    }

    public void clearAction() {
        currentAction = null;
    }
}
