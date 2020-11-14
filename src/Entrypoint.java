import rendering.engine.IRenderEngine;
import rendering.engine.RenderEngine;
import rendering.game.Entity;
import rendering.utility.FontsEngine;
import rendering.window.MainWindow;
import rendering.window.SplashWindow;

import javax.swing.*;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

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
        for (int i = 0; i < 30; i++) {
            Entity entity = renderEngine.addEntity("ressources/sprites/pacman.png", 28, 10);
            int x = ThreadLocalRandom.current().nextInt(0, MainWindow.WIDTH - 100);
            int y = ThreadLocalRandom.current().nextInt(0, MainWindow.HEIGHT - 100);
            entity.setPosition(x, y);
            float scale = ThreadLocalRandom.current().nextInt(1, 30) / 10f;

            entity.getSprite().setScale(scale);

            if (ThreadLocalRandom.current().nextBoolean())
                entity.getSprite().flipX();

            if (ThreadLocalRandom.current().nextBoolean())
                entity.getSprite().flipY();

            if (ThreadLocalRandom.current().nextBoolean())
                entity.getSprite().rotate(90);

            int delay = ThreadLocalRandom.current().nextInt(10, 100);
            entity.getSprite().loop(delay);
        }


    }

}
