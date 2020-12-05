package pacman.AI;

import framework.AI.IAIController;
import framework.AI.IAIModel;
import framework.geometry.Point;
import pacman.Game;
import pacman.Player;
import pacman.PlayerDirection;

public class GhostController implements IAIController {

    Player ghost;
    IAIModel ai;
    Point forcedTarget;
    private Point lastPosition;

    public GhostController(Player ghost) {
        forcedTarget = null;
        lastPosition = null;
        this.ghost = ghost;
    }

    public GhostController(Player ghost, IAIModel ai) {
        this(ghost);
        setAI(ai);
    }

    public void setAI(IAIModel ai) {
        this.ai = ai;
    }

    public void setForcedTarget(Point target) {
        forcedTarget = target;
    }

    public void clearForcedTarget() {
        forcedTarget = null;
    }

    private boolean prioritize(PlayerDirection direction, PlayerDirection other) {
        if (direction == other) return true;
        if (direction == PlayerDirection.UP) return true;
        if (direction == PlayerDirection.LEFT && other != PlayerDirection.UP) return true;
        if (direction == PlayerDirection.DOWN && other != PlayerDirection.UP && other != PlayerDirection.LEFT)
            return true;

        return false;
    }

    private PlayerDirection reverse(PlayerDirection direction) {
        switch (direction) {
            case UP:
                return PlayerDirection.DOWN;
            case DOWN:
                return PlayerDirection.UP;
            case RIGHT:
                return PlayerDirection.LEFT;
            case LEFT:
                return PlayerDirection.RIGHT;
        }

        return null;
    }

    public void update() {
        if ((lastPosition != null && lastPosition.equals(ghost.getPosition())) || ghost.isDisabled() || ai == null || !ghost.onTile()) return;

        // out of stage
        if (ghost.getX() < 0 || ghost.getX() > Game.current.map.width - Game.PLAYER_SIZE) return;
        if (ghost.getY() < 0 || ghost.getY() > Game.current.map.height - Game.PLAYER_SIZE) return;

        Point target = forcedTarget == null ? ai.getPrediction() : forcedTarget;
        PlayerDirection currentDirection = ghost.getDirection();

        Point currentPosition = ghost.getPosition();
        int shortestDistance = Integer.MAX_VALUE;
        PlayerDirection bestDirection = currentDirection;
        for (PlayerDirection direction : PlayerDirection.values()) {
            if (direction == reverse(currentDirection) || ghost.willHitWall(direction)) continue;

            int modX = direction == PlayerDirection.LEFT ? -Game.STEP_SIZE : direction == PlayerDirection.RIGHT ? Game.STEP_SIZE : 0;
            int modY = direction == PlayerDirection.UP ? -Game.STEP_SIZE : direction == PlayerDirection.DOWN ? Game.STEP_SIZE : 0;

            Point nextPosition = new Point(
                    currentPosition.getX() + modX,
                    currentPosition.getY() + modY
            );

            int distance = nextPosition.distanceSquared(target);

            if (distance < shortestDistance || (distance == shortestDistance && prioritize(direction, bestDirection))) {
                shortestDistance = distance;
                bestDirection = direction;
            }
        }

        if (bestDirection == currentDirection) return;

        ghost.changeDirection(bestDirection);
        lastPosition = new Point(ghost.getX(), ghost.getY());
    }

}
