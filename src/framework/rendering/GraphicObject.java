package framework.rendering;

import framework.geometry.Point;
import framework.rendering.graphics.Sprite;
import framework.rendering.graphics.SpriteSheet;

/**
 * Generic graphic representation of any object of the game
 */
public class GraphicObject {
    private final Point position;
    private final int id;
    private Sprite sprite;
    private boolean visible;

    public GraphicObject(Sprite sprite, int id) {
        this.sprite = sprite;
        this.id = id;
        position = new Point();
        visible = true;
    }

    /**
     * Returns the sprite of the object
     *
     * @return sprite
     */
    public Sprite getSprite() {
        return sprite;
    }

    /**
     * Set a new sprite to the graphic object
     *
     * @param sprite new sprite
     */
    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    /**
     * Set a new spritesheet for the current sprite
     *
     * @param spriteSheet new spriteSheet
     */
    public void setSpriteSheet(SpriteSheet spriteSheet) {
        this.sprite.setSpriteSheet(spriteSheet);

    }

    public void setPosition(int X, int Y) {
        position.set(X, Y);
    }

    /**
     * Returns the position of the object
     *
     * @return position
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Returns if the object is visible
     *
     * @return returns true if the object is visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Set the object visibility to visible
     */
    public void show() {
        visible = true;
    }

    /**
     * Set the object visibility to hidden
     */
    public void hide() {
        visible = false;
    }

    /**
     * Return the Id of the object
     *
     * @return Id of the object
     */
    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GraphicObject)) return false;

        GraphicObject other = (GraphicObject) obj;

        return getId() == other.getId();
    }

    @Override
    public String toString() {
        return "GraphicObject{" +
                "position=" + position +
                ", visible=" + visible +
                ", sprite=" + sprite +
                ", id=" + id +
                '}';
    }
}
