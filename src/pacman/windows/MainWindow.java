package pacman.windows;

import framework.rendering.RenderEngine;
import pacman.Pacman;

import javax.swing.*;
import java.awt.*;

/**
 * Main Window of the Game
 */
public class MainWindow extends JFrame {

    public static final int HEIGHT = 600;
    public static final int WIDTH = 600;
    public static final String TITLE = "Groupe 27 - PACMAN";

    private final RenderEngine renderEngine;

    public MainWindow(RenderEngine renderEngine) {
        super();
        this.renderEngine = renderEngine;
        build();
    }

    /**
     * Builds the main window
     */
    public void build() {
        setTitle(TITLE);
        setSize(new Dimension(WIDTH, HEIGHT));
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Pacman.panel.setSize(WIDTH, HEIGHT);
        Pacman.panel.setBackground(Color.black);
        setContentPane(Pacman.panel);
    }


}
