package pacman.AI;

import framework.AI.IAIModel;
import framework.geometry.Point;
import pacman.PacGame;
import pacman.Player;

public class BlinkyAI implements IAIModel {
    private Player pacman;

    public BlinkyAI(Player pacman) {
        this.pacman = pacman;
    }

    public static Point scatterPosition() {
        return new Point(PacGame.game.currentMap.width - PacGame.STEP_SIZE, 0);
    }

    public Point getPrediction() {
        return pacman.getCentredPosition();
    }
}
