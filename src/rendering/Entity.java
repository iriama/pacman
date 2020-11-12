package rendering;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Entity of the game (characters, obstacles etc..)
 */
public class Entity implements IEntity {
    private Sprite sprite;
    private final Point position;
    private final boolean visible;

    public Entity(Sprite sprite) {
        this.sprite = sprite;
        position = new Point();
        visible = false;
    }


    /**
     * Returns the sprite of the entity
     *
     * @return BufferedImage
     */
    public Sprite getSprite() {
        return sprite;
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
