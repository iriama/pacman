package rendering;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Entity of the game (characters, obstacles etc..)
 */
public class Entity implements IEntity {
    private BufferedImage sprite;
    private final Point position;
    private final boolean visible;

    public Entity() {
        sprite = null;
        position = new Point();
        visible = false;
    }

    /**
     * Returns the sprite of the entity
     *
     * @return BufferedImage
     */
    public BufferedImage getSprite() {
        return sprite;
    }

    /**
     * Set the sprite of the entity from a file path
     *
     * @param path path of the image file
     * @throws IOException image not found or invalid path
     */
    public void setSprite(String path) throws IOException {
        sprite = ImageIO.read(new File(path));
    }

    /**
     * Set the position of the entity
     *
     * @param X
     * @param Y
     */
    public void setPosition(int X, int Y) {
        position.X = X;
        position.Y = Y;
    }

    /**
     * Returns the position of the entity
     *
     * @return Point
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Return true if the entity should be visible
     *
     * @return boolean
     */
    public boolean isVisible() {
        return visible;
    }
}
