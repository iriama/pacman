package pacman.AI;

import framework.AI.IAIModel;
import framework.geometry.Point;
import pacman.Pacman;
import pacman.Player;
import pacman.PlayerDirection;

public class InkyAI implements IAIModel {

    Player pacman;
    Player blinky;

    public InkyAI(Player pacman, Player blinky) {
        this.pacman = pacman;
        this.blinky = blinky;
    }

    public static Point scatterPosition() {
        return new Point(Pacman.game.currentMap.height - Pacman.STEP_SIZE, Pacman.game.currentMap.width - Pacman.STEP_SIZE);
    }

    public Point getPrediction() {
        PlayerDirection pacmanDirection = pacman.getDirection();
        int modX = pacman.getModX(pacmanDirection, Pacman.TILE_SIZE * 2);
        int modY = pacman.getModY(pacmanDirection, Pacman.TILE_SIZE * 2);
        Point twoTilesAhead = new Point(
                modX,
                modY
        );
        Point intermediate = pacman.getCentredPosition().extend(twoTilesAhead);
        Point blinkyPosition = blinky.getCentredPosition();

        int offsetX = intermediate.getX() - blinkyPosition.getX();
        int offsetY = intermediate.getY() - blinkyPosition.getY();

        return intermediate.extend(new Point(
                offsetX,
                offsetY
        ));
    }
}
