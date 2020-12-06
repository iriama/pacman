package pacman.AI;

import framework.AI.IAIController;
import framework.AI.IAIModel;
import framework.geometry.Point;
import pacman.Game;
import pacman.game.Ghost;
import pacman.game.PlayerDirection;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GhostController implements IAIController {

    Ghost ghost;
    IAIModel ai;
    Point forcedTarget;
    private Point lastPosition;

    public GhostController(Ghost ghost) {
        forcedTarget = null;
        lastPosition = null;
        this.ghost = ghost;
    }

    public GhostController(Ghost ghost, IAIModel ai) {
        this(ghost);
        setAI(ai);
    }

    public static PlayerDirection reverse(PlayerDirection direction) {
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

    public void setAI(IAIModel ai) {
        this.ai = ai;
    }

    public void setForcedTarget(Point target) {
        forcedTarget = target;
    }

    public void clearForcedTarget() {
        forcedTarget = null;
    }

    public Point getTarget() {
        return forcedTarget == null ? ai.getPrediction() : forcedTarget;
    }

    private boolean prioritize(PlayerDirection direction, PlayerDirection other) {
        if (direction == other) return true;
        if (direction == PlayerDirection.UP) return true;
        if (direction == PlayerDirection.LEFT && other != PlayerDirection.UP) return true;
        if (direction == PlayerDirection.DOWN && other != PlayerDirection.UP && other != PlayerDirection.LEFT)
            return true;

        return false;
    }

    public void update() {
        if ((lastPosition != null && lastPosition.equals(ghost.getPosition())) || ghost.isDisabled() || (ai == null && forcedTarget == null) || !ghost.onTile())
            return;

        // out of stage
        if (ghost.getX() < 0 || ghost.getX() > Game.current.map.width - Game.PLAYER_SIZE) return;
        if (ghost.getY() < 0 || ghost.getY() > Game.current.map.height - Game.PLAYER_SIZE) return;

        Point target = getTarget();
        PlayerDirection currentDirection = ghost.getDirection();

        Point currentPosition = ghost.getPosition();
        int shortestDistance = Integer.MAX_VALUE;
        PlayerDirection bestDirection = currentDirection;

        boolean pickRandom = ghost.isFrightned();
        PlayerDirection[] array = PlayerDirection.values();
        if (pickRandom) {
            List<PlayerDirection> list = Arrays.asList(array);
            Collections.shuffle(list);
            array = (PlayerDirection[]) list.toArray();
        }

        for (PlayerDirection direction : array) {
            if (direction == reverse(currentDirection) || ghost.willHitWall(direction)) continue;

            if (pickRandom) {
                bestDirection = direction;
                break;
            }

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
