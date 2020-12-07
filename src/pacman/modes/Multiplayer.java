package pacman.modes;


import pacman.UI.Menu;
import pacman.UI.MultiGhost;
import pacman.UI.StatusBar;
import pacman.game.Game;

import java.util.Vector;

/**
 * Multiplayer mode
 */
public class Multiplayer {

    public static final int PRISON_TIME = 5000;
    public static final int FRIGHTNED_TIME = 7000;
    private static Menu menu;
    private static StatusBar statusBar;

    /**
     * Starts a multiplayer game
     *
     * @param menu   Menu
     * @param ghosts ghosts
     */
    public static void start(Menu menu, Vector<MultiGhost> ghosts) {
        Multiplayer.menu = menu;
        statusBar = new StatusBar();

        Game game = new Game();
        try {
            game.MultiGame(ghosts, statusBar, Multiplayer::onSuccess, Multiplayer::onFail);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void onSuccess(int score, int lives, int time) {
        menu.multiOverMenu("PACMAN won!");
        Game.mainWindow.setPanel(menu, Menu.WIDTH, Menu.HEIGHT);
    }

    private static void onFail(int score, int lives, int time) {
        menu.multiOverMenu("GHOSTS won!");
        Game.mainWindow.setPanel(menu, Menu.WIDTH, Menu.HEIGHT);
    }
}
