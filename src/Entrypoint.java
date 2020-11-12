import rendering.*;

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
        Entity pacman = new Entity(
                new Sprite(
                        new SpriteSheet("ressources/sprites/pacman01.png", 26, 26)
                )
        );
        renderEngine.addEntity(pacman);
        pacman.getSprite().loop(0, 2, 500);
    }

}
