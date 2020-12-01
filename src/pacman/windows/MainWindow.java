package pacman.windows;


import javax.swing.*;
import java.awt.*;

/**
 * Main Window of the Game
 */
public class MainWindow extends JFrame {
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
    public void setPanel(JPanel panel, int width, int height) {
        setContentPane(panel);
        getContentPane().setPreferredSize(new Dimension(width, height));
        pack();
        // workaround
        Dimension size = getContentPane().getPreferredSize();
        getContentPane().setPreferredSize(new Dimension(width - (size.width - width), height - (size.height - height)));
        pack();
        setLocationRelativeTo(null);
        toFront();
    }
}
