package framework.rendering;

import framework.geometry.Point;
import framework.rendering.graphics.Sprite;
import framework.rendering.graphics.SpriteSheet;
import framework.utility.IdFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Vector;

/**
 * Engine responsable of framework.rendering graphic objects
 */
public class RenderEngine implements IRenderEngine, IPaintComponentListener {

    private final Vector<GraphicObject> objects;
    private final HashMap<String, SpriteSheet> spriteSheetCache;
    private final RenderPanel panel;
    private Point camera;

    public RenderEngine() {
        objects = new Vector<GraphicObject>();
        spriteSheetCache = new HashMap<String, SpriteSheet>();
        camera = new Point(0, 0);
        panel = new RenderPanel();
        panel.addOnPaintComponentListener(this);
        panel.setBackground(Color.black);
    }


    private boolean shouldDraw(GraphicObject object) {
        Point position = object.getPosition();

        int minX = camera.getX();
        int minY = camera.getY();
        int maxX = minX + panel.getWidth();
        int maxY = minY + panel.getHeight();

        return object.isVisible(); //&& position.getX() >= minX && position.getY() >= minY && position.getX() <= maxX && position.getY() <= maxY;
    }


    private Point translate(Point position) {
        return new Point(position.getX() - camera.getX(), position.getY() - camera.getY());
    }

    /**
     * Adds an object to the render engine
     *
     * @param object object to add
     * @return added object
     */
    public GraphicObject addObject(GraphicObject object) {
        objects.add(object);
        return object;
    }

    /**
     * Adds an object to the render engine
     *
     * @param spriteSheetPath path to the sprite sheet file
     * @param spriteWidth     width of one sprite
     * @param spriteCount     number of sprites on the sprite sheet
     * @return the added object
     */
    public GraphicObject addObject(String spriteSheetPath, int spriteWidth, int spriteCount) {
        SpriteSheet sheet;
        if (spriteSheetCache.containsKey(spriteSheetPath))
            sheet = spriteSheetCache.get(spriteSheetPath);
        else
            sheet = new SpriteSheet(spriteSheetPath, spriteWidth, spriteCount);

        Sprite sprite = new Sprite(sheet);
        GraphicObject GraphicObject = new GraphicObject(sprite, IdFactory.nextId());

        return addObject(GraphicObject);
    }

    /**
     * Removes an GraphicObject from the render engine
     *
     * @param graphicObject GraphicObject to remove
     */
    public void removeObject(GraphicObject graphicObject) {
        objects.remove(graphicObject);
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
        for (int i = 0; i < objects.size(); i++) {
            GraphicObject object = objects.get(i);
            if (!shouldDraw(object)) continue;

            try {
                Point position = translate(object.getPosition());
                BufferedImage image = object.getSprite().getImage();

                g.drawImage(image, position.getX(), position.getY(), null);
            } catch (Exception e) {
                System.err.println("can't paint " + object);
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
