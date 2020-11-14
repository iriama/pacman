package rendering.engine;

import rendering.game.Entity;
import rendering.game.Point;
import rendering.graphics.Sprite;
import rendering.graphics.SpriteSheet;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Vector;

/**
 * Engine responsable of rendering entities and other game elements
 */
public class RenderEngine implements IRenderEngine, IPaintComponentListener {

    private final Vector<Entity> entities;
    private final HashMap<String, SpriteSheet> spriteSheetCache;
    private final RenderPanel panel;
    private int entityIds;

    public RenderEngine() {
        entityIds = Integer.MIN_VALUE;
        entities = new Vector<Entity>();
        spriteSheetCache = new HashMap<String, SpriteSheet>();
        panel = new RenderPanel();
        panel.addOnPaintComponentListener(this);

        panel.setBackground(Color.black);
    }


    private boolean shouldDraw(Entity entity) {
        rendering.game.Point position = entity.getPosition();

        return entity.isVisible() && position.getX() >= 0 && position.getY() >= 0 && position.getX() <= panel.getWidth() && position.getY() <= panel.getHeight();
    }

    private int nextId() {
        if (entityIds > Integer.MAX_VALUE - 2)
            entityIds = Integer.MIN_VALUE;

        entityIds++;
        return entityIds;
    }

    /**
     * Adds an entity to the render engine
     * @param entity entity to add
     * @return added entity
     */
    public Entity addEntity(Entity entity) {
        entities.add(entity);
        return entity;
    }

    /**
     * Adds an entity to the render engine
     * @param spriteSheetPath path to the sprite sheet file
     * @param spriteWidth width of one sprite
     * @param spriteCount number of sprites on the sprite sheet
     * @return the added entity
     */
    public Entity addEntity(String spriteSheetPath, int spriteWidth, int spriteCount) {
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
     * @param entity entity to remove
     */
    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    /**
     * Removes an entity from the render engine by id
     * @param entityId id of the entity to remove
     */
    public void removeEntity(int entityId) {
        removeEntity(new Entity(null, entityId));
    }

    /**
     * Return the JPanel instance
     * @return JPanel instance
     */
    public RenderPanel getPanel() {
        return panel;
    }

    /**
     * To be called on every onPaint JPanel API
     * @param g Graphics2D brush
     */
    public void onPaint(Graphics2D g) {
        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            if (!shouldDraw(entity)) continue;

            try {
                Point position = entity.getPosition();
                BufferedImage image = entity.getSprite().getImage();

                g.drawImage(image, position.getX(), position.getY(), null);
            } catch (Exception e) {
                System.err.println("can't paint " + entity);
            }
        }
    }
}
