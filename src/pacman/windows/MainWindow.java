package pacman.windows;

import framework.rendering.RenderEngine;
import pacman.PacmanDemo;

import javax.swing.*;
import java.awt.*;

/**
 * Main Window of the Game
 */
public class MainWindow extends JFrame {

    public static final int HEIGHT = 600;
    public static final int WIDTH = 600;
    public static final String TITLE = "Groupe 27 - PACMAN";

    private JPanel panel;

    public MainWindow(JPanel panel) {
        super();
        this.panel = panel;
        build();
    }

    /**
     * Builds the main window
     */
    public void build() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle(TITLE);
        setResizable(false);
        panel.setBackground(Color.black);
        setContentPane(panel);
        getContentPane().setPreferredSize(new Dimension(WIDTH, HEIGHT));
        pack();
        setLocationRelativeTo(null);
    }


}
