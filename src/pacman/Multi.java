package pacman;


import java.util.Vector;

public class Multi {

    private static Menu menu;
    private static StatusBar statusBar;
    public static final int PRISON_TIME = 5000;
    public static final int FRIGHTNED_TIME = 7000;

    public static void start(Menu menu, Vector<GPanel> ghosts) {
        Multi.menu = menu;
        statusBar = new StatusBar();

        Game game = new Game();
        try {
            game.MultiGame(ghosts, statusBar, (s, l, t) -> onSuccess(s, l, t), (s, l, t) -> onFail(s, l, t));
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
