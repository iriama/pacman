package pacman;

import framework.IGameEngine;
import framework.core.CoreEngine;
import framework.input.InputEngine;
import framework.physics.PhysicsEngine;
import framework.rendering.RenderEngine;
import pacman.utility.FontsEngine;
import pacman.windows.MainWindow;
import pacman.windows.SplashWindow;

import javax.swing.*;

public class Pacman implements IGameEngine {

    private RenderEngine renderEngine;
    private PhysicsEngine physicsEngine;
    private InputEngine inputEngine;
    private CoreEngine coreEngine;

    private Menu menu;
    private Game game;

    public Pacman() {
        menu = new Menu();
        game = new Game();
        renderEngine = new RenderEngine(game);
        game.setRenderEngine(renderEngine);
        physicsEngine = new PhysicsEngine();
        game.setPhysicsEngine(physicsEngine);
        inputEngine = new InputEngine();
        coreEngine = new CoreEngine(renderEngine, physicsEngine, inputEngine, this);
        game.setCoreEngine(coreEngine);
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
        mainWindow.setPanel(game);
    }


    public static void main(String[] args) {
        Pacman game = new Pacman();
        game.launch();
    }

    public void update() {

    }
}
