package pacman;

import pacman.utility.FontsEngine;
import pacman.windows.MainWindow;
import pacman.windows.SplashWindow;
import sun.applet.Main;

import javax.swing.*;

public class Entrypoint {

    public static void main(String[] args) throws InterruptedException {
        for (String arg : args) {
            if (!arg.startsWith("-")) continue;
            switch (arg) {
                case "-debug":
                    Game.DEBUG = true;
                    break;
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
