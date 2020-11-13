package rendering.window;

import rendering.utility.FontsEngine;

import javax.swing.*;
import java.awt.*;

/**
 * Splash window of the game logo
 */
public class SplashWindow extends JFrame implements IWindow {

    public SplashWindow() {
        super();
        build();
    }

    /**
     * Builds the splash window
     */
    public void build() {
        JLabel label = new JLabel();
        label.setText("PACMAN");
        label.setFont(new Font(FontsEngine.getFontName("pacfont"), Font.PLAIN, 24));
        label.setForeground(new Color(229, 219, 0));

        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(label);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        setLocationRelativeTo(null);
        setSize(new Dimension(200, 200));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(panel);
        setAlwaysOnTop(true);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        pack();
    }
}
