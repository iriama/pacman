package rendering.window;

import rendering.engine.IRenderEngine;

import javax.swing.*;
import java.awt.*;

/**
 * Main Window of the Game
 */
public class MainWindow extends JFrame implements IWindow {

    public static final int HEIGHT = 600;
    public static final int WIDTH = 600;

    private final IRenderEngine renderEngine;

    public MainWindow(IRenderEngine renderEngine) {
        super();
        this.renderEngine = renderEngine;
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

        renderEngine.getPanel().setSize(WIDTH, HEIGHT);
        setContentPane(renderEngine.getPanel());
    }


}
