package nz.ac.vuw.ecs.swen225.gp22.domain;

public class Teleport implements TileType {
    private Position target = null;

    private int doTeleport = 0;

    public void setTarget(Position target) {
        this.target = target;
    }

    @Override
    public ActionType interact(Tile self, Action action) {
        doTeleport++;
        return ActionType.TP;
    }

    @Override
    public boolean onPing(Tile self, Actor actor) {
        if (target == null)
            throw new IllegalStateException("Teleporter at " + self.position() + " has no target");

        if (doTeleport > 0) {
            actor.setPosition(target);
            doTeleport--;
            return true;
        }

        return false;
    }

    @Override
    public boolean isWalkable() {
        return true;
    }

    @Override
    public String toString() {
        return "@";
    }

    @Override
    public String name() {
        return "TP";
    }

    public Position target() {
        if (target == null)
            throw new IllegalStateException("Teleporter has no target");
        return target;
    }

    public boolean doTeleport() {
        return doTeleport > 0;
    }
}