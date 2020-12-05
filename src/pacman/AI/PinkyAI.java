package pacman.AI;

import framework.AI.IAIModel;
import framework.geometry.Point;
import pacman.PacGame;
import pacman.Player;
import pacman.PlayerDirection;

public class PinkyAI implements IAIModel {

    Player pacman;

    public PinkyAI(Player pacman) {
        this.pacman = pacman;
    }

    public static Point scatterPosition() {
        return new Point(0, 0);
    }

    public Point getPrediction() {
        PlayerDirection pacmanDirection = pacman.getDirection();
        int modX = pacman.getModX(pacmanDirection, PacGame.TILE_SIZE * 4);
        int modY = pacman.getModY(pacmanDirection, PacGame.TILE_SIZE * 4);

        Point fourTilesAhead = new Point(
                modX,
                modY
        );
        return pacman.getCentredPosition().extend(fourTilesAhead);
    }
}

