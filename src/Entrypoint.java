import core.engine.CoreEngine;
import physics.engine.PhysicsEngine;
import rendering.engine.RenderEngine;
import rendering.utility.FontsEngine;
import rendering.window.MainWindow;
import rendering.window.SplashWindow;

import javax.swing.*;

public class Entrypoint {

    public static void main(String[] args) throws InterruptedException {
        FontsEngine.start();

        RenderEngine renderEngine = new RenderEngine();


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

        // Test
        PhysicsEngine physicsEngine = new PhysicsEngine();
        CoreEngine coreEngine = new CoreEngine(renderEngine, physicsEngine);

        coreEngine.addCharacter(0, 28, 0, 28, 2, 0, "ressources/sprites/pacman.png", 28, 10, 10);

        coreEngine.run();
    }

}
