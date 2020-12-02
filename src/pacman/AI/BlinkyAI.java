package pacman.AI;

import framework.AI.IAIModel;
import framework.geometry.Point;
import pacman.Player;

public class BlinkyAI implements IAIModel {
    private Player ghost;
    private Player pacman;

    public BlinkyAI(Player ghost, Player pacman) {
        this.ghost = ghost;
        this.pacman = pacman;
    }

    public Point getPrediction() {
        return pacman.getPosition();
    }
}
