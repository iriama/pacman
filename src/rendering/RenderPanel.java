package rendering;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class RenderPanel extends JPanel {

    private static final int SLEEP_MS = 10;

    public Vector<IDrawEvent> drawEvents;

    public RenderPanel() {
        drawEvents = new Vector<IDrawEvent>();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (IDrawEvent e: drawEvents)
            e.draw((Graphics2D) g);

        try {
            Thread.sleep(SLEEP_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        repaint();
    }

}
