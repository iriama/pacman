package rendering.engine;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class RenderPanel extends JPanel {

    private static final int SLEEP_MS = 10;

    private final Vector<IPaintComponentListener> paintComponentListeners;

    public RenderPanel() {
        paintComponentListeners = new Vector<IPaintComponentListener>();
    }

    public void addOnPaintComponentListener(IPaintComponentListener listener) {
        paintComponentListeners.add(listener);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (IPaintComponentListener e : paintComponentListeners)
            e.onPaint((Graphics2D) g);

        try {
            Thread.sleep(SLEEP_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        repaint();
    }

}
