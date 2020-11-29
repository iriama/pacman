package pacman;

import pacman.utility.FontsEngine;

import javax.swing.*;
import java.awt.*;

public class Menu extends JPanel {
    public Menu() {
        super();
        setBackground(Color.black);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.green);
        g.drawString("-- Menu", 0, g.getFont().getSize());


    }
}
