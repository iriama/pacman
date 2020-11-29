package pacman.windows;

import framework.rendering.RenderEngine;
import pacman.PacmanDemo;

import javax.swing.*;
import java.awt.*;

/**
 * Main Window of the Game
 */
public class MainWindow extends JFrame {

    public static final int HEIGHT = 550;
    public static final int WIDTH = 450;
    public static final String TITLE = "Groupe 27 - PACMAN";

    public MainWindow() {
        super();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle(TITLE);
        setResizable(false);
    }

    /**
     * Set the main panel
     * @param panel main panel
     */
    public void setPanel(JPanel panel) {
        setContentPane(panel);
        getContentPane().setPreferredSize(new Dimension(WIDTH, HEIGHT));
        pack();
        setLocationRelativeTo(null);
        toFront();
    }

}
