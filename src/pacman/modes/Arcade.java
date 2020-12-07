package pacman.modes;

import pacman.UI.Menu;
import pacman.UI.StatusBar;
import pacman.game.Game;
import pacman.game.Score;
import pacman.parsing.MemoryDB;

/**
 * Arcade mode
 */
public class Arcade {

    private static int currentLevel;
    private static int score;
    private static String preset;
    private static String name;
    private static Menu menu;

    private static final String[] levels = MemoryDB.getArcadeSequence();
    private static StatusBar statusBar;

    /**
     * Starts arcade mode
     *
     * @param menu   Menu
     * @param name   player name
     * @param preset player key preset id
     */
    public static void start(Menu menu, String name, String preset) {
        Arcade.preset = preset;
        Arcade.name = name;
        Arcade.menu = menu;
        currentLevel = 0;
        score = 0;
        statusBar = new StatusBar();
        next();
    }

    private static void next() {
        String level = levels[currentLevel];
        StatusBar.WIDTH = 90 + level.length() * 5;
        Game game = new Game();
        game.SoloGame(score, level, preset, statusBar, Arcade::onSuccess, Arcade::onFail);
    }


    private static void onSuccess(int score, int lives, int time) {
        Arcade.score = score;
        currentLevel++;
        if (currentLevel > levels.length - 1) {
            Score s = new Score(name, Arcade.score);
            if (Arcade.score > 0)
                MemoryDB.updateScores(s);
            menu.scoreMenu("YOU WON !");
            Game.mainWindow.setPanel(menu, Menu.WIDTH, Menu.HEIGHT);
            return;
        }
        next();
    }

    private static void onFail(int score, int lives, int time) {
        Arcade.score = score;
        Score s = new Score(name, Arcade.score);
        if (Arcade.score > 0)
            MemoryDB.updateScores(s);

        menu.scoreMenu("GAMEOVER");
        Game.mainWindow.setPanel(menu, Menu.WIDTH, Menu.HEIGHT);
    }

}
