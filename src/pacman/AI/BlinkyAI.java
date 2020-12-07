package pacman.AI;

import framework.AI.IAIModel;
import framework.geometry.Point;
import pacman.game.Game;
import pacman.game.Player;

/**
 * Blinky AI
 */
public class BlinkyAI implements IAIModel {
    private final Player pacman;

    public BlinkyAI(Player pacman) {
        this.pacman = pacman;
    }

    public static Point scatterPosition() {
        return new Point(Game.current.map.width - Game.STEP_SIZE, 0);
    }

    public Point getPrediction() {
        return pacman.getCentredPosition();
    }
}
