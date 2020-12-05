package pacman.AI;

import framework.AI.IAIModel;
import framework.geometry.Point;
import pacman.PacGame;
import pacman.Player;

public class ClydeAI implements IAIModel {

    Player pacman;
    Player ghost;

    public ClydeAI(Player pacman, Player ghost) {
        this.pacman = pacman;
        this.ghost = ghost;
    }

    public static Point scatterPosition() {
        return new Point(0, PacGame.game.currentMap.height - PacGame.STEP_SIZE);
    }

    public Point getPrediction() {

        Point pacmanPosition = pacman.getCentredPosition();
        Point clydePosition = ghost.getPosition();

        if (pacmanPosition.distanceSquared(clydePosition) < PacGame.TILE_SIZE * 8)
            return scatterPosition();

        return pacmanPosition;
    }
}
