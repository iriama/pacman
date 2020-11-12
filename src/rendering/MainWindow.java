package rendering;

import javax.swing.*;
import java.awt.*;

/**
 * Main Window of the Game
 */
public class MainWindow extends JFrame implements IBuild {

    public static final int HEIGHT = 600;
    public static final int WIDTH = 600;

    public MainWindow() {
        super();
        build();
    }

    /**
     * Builds the main window
     */
    public void build() {
        setTitle("Groupe 27 - PACMAN");
        setSize(new Dimension(WIDTH, HEIGHT));
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel render = new RenderEngine();
        render.setSize(WIDTH, HEIGHT);

        //TEST
        Entity pacman = new Entity();
        pacman.setPosition(20, 20);
        try {
            pacman.setSprite("ressources/sprites/pacman01.png");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        IRenderEngine renderEngine = (IRenderEngine) render;
        renderEngine.addEntity(pacman);


        setContentPane(render);
    }


}
