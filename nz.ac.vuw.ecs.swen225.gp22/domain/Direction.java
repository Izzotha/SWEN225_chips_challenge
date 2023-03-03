package nz.ac.vuw.ecs.swen225.gp22.domain;

/**
 * A direction an actor can move in
 *
 * @author Ninos Awas
 */
public enum Direction {
    UP {
        @Override
        public Position toPoint() {
            return new Position(0, -1);
        }
    },
    DOWN {
        @Override
        public Position toPoint() {
            return new Position(0, 1);
        }
    },
    LEFT {
        @Override
        public Position toPoint() {
            return new Position(-1, 0);
        }
    },
    RIGHT {
        @Override
        public Position toPoint() {
            return new Position(1, 0);
        }
    };

    public abstract Position toPoint();
}
