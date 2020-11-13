import rendering.engine.IRenderEngine;
import rendering.engine.RenderEngine;
import rendering.game.Entity;
import rendering.utility.FontsEngine;
import rendering.window.MainWindow;
import rendering.window.SplashWindow;

import javax.swing.*;
import java.io.IOException;

public class Entrypoint {

    public static void main(String[] args) throws InterruptedException, IOException {
        FontsEngine.start();

        IRenderEngine renderEngine = new RenderEngine();

        JFrame splash = new SplashWindow();
        JFrame win = new MainWindow(renderEngine);

        SwingUtilities.invokeLater(() -> {
            splash.setVisible(true);
        });

        Thread.sleep(2000);

        SwingUtilities.invokeLater(() -> {
            splash.setVisible(false);
            win.setVisible(true);
        });

        // TEST
        Entity pacman = renderEngine.addEntity("ressources/sprites/pacman01.png", 26, 26);
        pacman.getSprite().loop(0, 2, 1000);
    }

}
