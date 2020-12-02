package pacman.AI;

import framework.AI.IAIController;
import framework.AI.IAIModel;
import framework.geometry.Point;
import pacman.Pacman;
import pacman.Player;
import pacman.PlayerDirection;

public class GhostController implements IAIController {

    Player ghost;
    IAIModel ai;

    public GhostController(Player ghost) {
        this.ghost = ghost;
    }

    public GhostController(Player ghost, IAIModel ai) {
        this(ghost);
        setAI(ai);
    }

    public void setAI(IAIModel ai) {
        this.ai = ai;
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
        if (ai == null || !ghost.onTile()) return;

        Point target = ai.getPrediction();
        PlayerDirection currentDirection = ghost.getDirection();

        Point currentPosition = ghost.getPosition();
        int shortestDistance = Integer.MAX_VALUE;
        PlayerDirection bestDirection = currentDirection;
        for (PlayerDirection direction : PlayerDirection.values()) {
            if (direction == reverse(currentDirection) || ghost.willHitWall(direction)) continue;

            int modX = direction == PlayerDirection.LEFT ? -Pacman.STEP_SIZE : direction == PlayerDirection.RIGHT ? Pacman.STEP_SIZE : 0;
            int modY = direction == PlayerDirection.UP ? -Pacman.STEP_SIZE : direction == PlayerDirection.DOWN ? Pacman.STEP_SIZE : 0;

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
    }

}
