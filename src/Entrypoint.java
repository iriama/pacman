import rendering.FontsEngine;
import rendering.MainWindow;
import rendering.SplashWindow;

import javax.swing.*;

public class Entrypoint {

    public static void main(String[] args) throws InterruptedException {
        FontsEngine.start();

        JFrame win = new MainWindow();
        JFrame splash = new SplashWindow();


        SwingUtilities.invokeLater(() -> {
            splash.setVisible(true);
        });

        Thread.sleep(2000);

        SwingUtilities.invokeLater(() -> {
            splash.setVisible(false);
            win.setVisible(true);
        });
    }

}
