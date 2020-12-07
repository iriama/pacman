package pacman.game;

/**
 * Game won/lost event
 */
public interface IGameResult {
    /**
     * Action on game won/lost
     *
     * @param score current score
     * @param lives remaining lives
     * @param time  elapsed time
     */
    void action(int score, int lives, int time);
}
