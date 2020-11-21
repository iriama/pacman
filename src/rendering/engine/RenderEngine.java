package rendering.engine;

import core.engine.IEngine;
import geometry.Point;
import rendering.game.Entity;
import rendering.game.IEntity;
import rendering.graphics.Sprite;
import rendering.graphics.SpriteSheet;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Vector;

/**
 * Engine responsable of rendering entities and other game elements
 */
public class RenderEngine implements IRenderEngine, IPaintComponentListener, IEngine {

    private final Vector<IEntity> entities;
    private final HashMap<String, SpriteSheet> spriteSheetCache;
    private final RenderPanel panel;
    private int entityIds;
    private Point camera;

    public RenderEngine() {
        entityIds = Integer.MIN_VALUE;
        entities = new Vector<IEntity>();
        spriteSheetCache = new HashMap<String, SpriteSheet>();
        camera = new Point(0, 0);
        panel = new RenderPanel();
        panel.addOnPaintComponentListener(this);
        panel.setBackground(Color.black);
    }


    private boolean shouldDraw(IEntity entity) {
        Point position = entity.getPosition();

        int minX = camera.getX();
        int minY = camera.getY();
        int maxX = minX + panel.getWidth();
        int maxY = minY + panel.getHeight();

        return entity.isVisible() && position.getX() >= minX && position.getY() >= minY && position.getX() <= maxX && position.getY() <= maxY;
    }


    private Point translate(Point position) {
        return new Point(position.getX() - camera.getX(), position.getY() - camera.getY());
    }

    private int nextId() {
        if (entityIds > Integer.MAX_VALUE - 2)
            entityIds = Integer.MIN_VALUE;

        entityIds++;
        return entityIds;
    }

    /**
     * Adds an entity to the render engine
     *
     * @param entity entity to add
     * @return added entity
     */
    public IEntity addEntity(IEntity entity) {
        entities.add(entity);
        return entity;
    }

    /**
     * Adds an entity to the render engine
     *
     * @param spriteSheetPath path to the sprite sheet file
     * @param spriteWidth     width of one sprite
     * @param spriteCount     number of sprites on the sprite sheet
     * @return the added entity
     */
    public IEntity addEntity(String spriteSheetPath, int spriteWidth, int spriteCount) {
        SpriteSheet sheet;
        if (spriteSheetCache.containsKey(spriteSheetPath))
            sheet = spriteSheetCache.get(spriteSheetPath);
        else
            sheet = new SpriteSheet(spriteSheetPath, spriteWidth, spriteCount);

        Sprite sprite = new Sprite(sheet);
        Entity entity = new Entity(sprite, nextId());

        return addEntity(entity);
    }

    /**
     * Removes an entity from the render engine
     *
     * @param entity entity to remove
     */
    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    /**
     * Removes an entity from the render engine by id
     *
     * @param entityId id of the entity to remove
     */
    public void removeEntity(int entityId) {
        removeEntity(new Entity(null, entityId));
    }

    /**
     * Return the JPanel instance
     *
     * @return JPanel instance
     */
    public RenderPanel getPanel() {
        return panel;
    }

    /**
     * To be called on every onPaint JPanel API
     *
     * @param g Graphics2D brush
     */
    public void onPaint(Graphics2D g) {
        for (int i = 0; i < entities.size(); i++) {
            IEntity entity = entities.get(i);
            if (!shouldDraw(entity)) continue;

            try {
                Point position = translate(entity.getPosition());
                BufferedImage image = entity.getSprite().getImage();

                g.drawImage(image, position.getX(), position.getY(), null);
            } catch (Exception e) {
                System.err.println("can't paint " + entity);
            }
        }
    }

    public void update() {
        panel.repaint();
    }

    /**
     * Returns the postion of the camera
     *
     * @return position of the camera
     */
    public Point getCameraPosition() {
        return camera;
    }

    /**
     * Sets the position of the camera
     *
     * @param x x coords
     * @param y y coords
     */
    public void moveCamera(int x, int y) {
        camera.set(x, y);
    }
}
