package pacman.testing;

import pacman.utility.FontsEngine;
import pacman.windows.MainWindow;
import pacman.windows.SplashWindow;

import javax.swing.*;

public class Pacman {

    private Menu menu;
    private Game game;

    public Pacman() {
        menu = new Menu();
        game = new Game();
    }

    protected void launch() {
        // Starting the fonts engine
        FontsEngine.start();

        SplashWindow splash = new SplashWindow();
        MainWindow mainWindow = new MainWindow();

        // Show 1 second splash screen
        /*SwingUtilities.invokeLater(() -> splash.setVisible(true));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) { e.printStackTrace(); }
        */

        // Show main window
        SwingUtilities.invokeLater(() -> {
            splash.setVisible(false);
            mainWindow.setVisible(true);
        });

        // Show menu
        //mainWindow.setPanel(menu);
        mainWindow.setPanel(game, 500, 500);
        game.start();
    }


    public static void main(String[] args) {
        Pacman game = new Pacman();
        game.launch();
    }

}
