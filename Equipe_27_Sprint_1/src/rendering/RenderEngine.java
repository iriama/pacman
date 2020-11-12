package rendering;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Vector;

/**
 * Engine responsable of rendering entities and other game elements
 */
public class RenderEngine extends JPanel implements IRenderEngine, IBuild {

    private final Vector<Entity> entities;

    public RenderEngine() {
        super();

        entities = new Vector<Entity>();
        build();
    }

    /**
     * Builds the render engine
     */
    public void build() {
        setBackground(Color.black);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        draw((Graphics2D) g);
    }


    private boolean shouldDraw(Entity entity) {
        Point position = entity.getPosition();

        return !entity.isVisible() || position.X < 0 || position.Y < 0 || position.X > MainWindow.WIDTH || position.Y > MainWindow.HEIGHT;
    }


    /**
     * Adds an entity to the render engine
     *
     * @param entity entity
     */
    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    /**
     * Draws game elements
     *
     * @param g Graphics2D
     */
    public void draw(Graphics2D g) {

        for (Entity entity : entities) {
            if (!shouldDraw(entity)) continue;

            Point position = entity.getPosition();
            BufferedImage sprite = entity.getSprite();

            g.drawImage(sprite, position.X, position.Y, null);
        }

    }

}
