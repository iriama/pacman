package pacman;

import pacman.UI.Menu;
import pacman.game.Game;
import pacman.modes.Arcade;
import pacman.utility.FontsEngine;
import pacman.windows.MainWindow;
import pacman.windows.SplashWindow;

import javax.swing.*;

/**
 * Entrypoint of the program
 */
public class Entrypoint {

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (!arg.startsWith("-")) continue;
            if ("-debug".equals(arg)) {
                Game.DEBUG = true;
            } else if ("-level".equals(arg) && args.length > i + 1) {
                Arcade.forceLevel = Integer.parseInt(args[i + 1]) - 1;
            }
        }

        FontsEngine.start();

        MainWindow mainWindow = new MainWindow();
        Game.mainWindow = mainWindow;
        SplashWindow splashWindow = new SplashWindow();
        Menu menu = new Menu();
        mainWindow.setPanel(menu, Menu.WIDTH, Menu.HEIGHT);

        SwingUtilities.invokeLater(() -> splashWindow.setVisible(true));

        Thread.sleep(500);

        SwingUtilities.invokeLater(() -> {
            splashWindow.setVisible(false);
            mainWindow.setVisible(true);
        });
    }
}
