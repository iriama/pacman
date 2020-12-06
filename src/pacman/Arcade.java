package pacman;

import pacman.parsing.MemoryDB;

public class Arcade {

    private static int currentLevel;
    private static int score;
    private static String preset;
    private static String name;
    private static Menu menu;

    private static String[] levels = MemoryDB.getArcadeSequence();
    private static StatusBar statusBar;

    public static void start(Menu menu, String name, String preset) {
        Arcade.preset = preset;
        Arcade.name = name;
        Arcade.menu = menu;
        System.out.println(name);
        currentLevel = 0;
        score = 0;
        statusBar = new StatusBar();
        next();
    }

    private static void next() {
        String level = levels[currentLevel];
        statusBar.WIDTH = 90 + level.length()*5;
        Game game = new Game();
        game.SoloGame(score, level, preset, statusBar, (s, l, t) -> onSuccess(s, l, t), (s, l, t) -> onFail(s, l, t));
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
