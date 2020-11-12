package rendering;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Vector;

/**
 * Engine responsable of rendering entities and other game elements
 */
public class RenderEngine implements IRenderEngine {

    private final Vector<Entity> entities;
    private RenderPanel panel;

    public RenderEngine() {
        RenderEngine _this = this;
        entities = new Vector<Entity>();
        panel = new RenderPanel();

        panel.drawEvents.add(new IDrawEvent() {
            @Override
            public void draw(Graphics2D g) {
                _this.draw(g);
            }
        });

        panel.setBackground(Color.black);
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

    @Override
    public void addEntity(String spritePath, int spriteWidth, int spriteCount) {
        SpriteSheet sheet = new SpriteSheet(spritePath, spriteWidth, spriteCount);
        Sprite sprite = new Sprite(sheet);
        Entity entity = new Entity(sprite);

        entities.add(entity);
    }

    /**
     * Draws game elements
     *
     * @param g Graphics2D
     */
    public void draw(Graphics2D g) {
        System.out.println("drawing");
        for (Entity entity : entities) {
            if (!shouldDraw(entity)) continue;

            Point position = entity.getPosition();
            BufferedImage image = entity.getSprite().getImage();

            g.drawImage(image, position.X, position.Y, null);
        }
    }

    @Override
    public RenderPanel getPanel() {
        return panel;
    }
}
