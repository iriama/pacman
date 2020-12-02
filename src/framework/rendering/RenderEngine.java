package framework.rendering;

import framework.geometry.Point;
import framework.rendering.graphics.Sprite;
import framework.rendering.graphics.SpriteSheet;
import framework.utility.IdFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Vector;

/**
 * Engine responsable of framework.rendering graphic objects
 */
public class RenderEngine implements IRenderEngine {

    private final Vector<GraphicObject> objects;
    private Point camera;
    private IPanel panel;

    public RenderEngine(IPanel panel) {
        objects = new Vector<GraphicObject>();
        camera = new Point(0, 0);
        this.panel = panel;
    }

    /**
     * Creates an object
     *
     * @param spriteSheetPath path to the sprite sheet file
     * @param spriteWidth     width of one sprite
     * @param spriteCount     number of sprites on the sprite sheet
     * @return graphic object
     */
    public static GraphicObject createObject(String spriteSheetPath, int spriteWidth, int spriteCount) throws IOException {
        return createObject(new SpriteSheet(spriteSheetPath, spriteWidth, spriteCount));
    }

    /**
     * Creates an object from spriteSheet
     *
     * @param spriteSheet spriteSheet
     * @return graphic object
     */
    public static GraphicObject createObject(SpriteSheet spriteSheet) {
        Sprite sprite = new Sprite(spriteSheet);
        return new GraphicObject(sprite, IdFactory.nextId());
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
     * Removes an GraphicObject from the render engine
     *
     * @param graphicObject GraphicObject to remove
     */
    public void removeObject(GraphicObject graphicObject) {
        objects.remove(graphicObject);
    }

    /**
     * Paint scene into the graphics object provided
     *
     * @param g Graphics2D object
     */
    public void draw(Graphics2D g) {
        for (int i = 0; i < objects.size(); i++) {
            GraphicObject object = objects.get(i);
            if (!object.isVisible()) continue;

            try {
                Point position = translate(object.getPosition());
                BufferedImage image = object.getSprite().getImage();

                g.drawImage(image, position.getX(), position.getY(), null);
            } catch (Exception e) {
                System.err.println("can't draw " + object);
            }
        }
    }

    /**
     * Updates the render engine
     */
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
