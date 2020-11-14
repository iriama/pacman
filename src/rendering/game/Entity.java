package rendering.game;

import rendering.graphics.Sprite;

/**
 * Generic graphic representation of any entity of the game
 */
public class Entity implements IEntity {
    private final Point position;
    private boolean visible;
    private final Sprite sprite;
    private final int id;

    public Entity(Sprite sprite, int id) {
        this.sprite = sprite;
        this.id = id;
        position = new Point();
        visible = true;
    }

    /**
     * Returns the sprite of the entity
     * @return sprite
     */
    public Sprite getSprite() {
        return sprite;
    }

    public void setPosition(int X, int Y) {
        position.setX(X);
        position.setY(Y);
    }

    /**
     * Returns the position of the entity
     * @return position
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Returns if the entity is visible
     * @return returns true if the entity is visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Set the entity visibility to visible
     */
    public void show() {
        visible = true;
    }

    /**
     * Set the entity visibility to hidden
     */
    public void hide() {
        visible = false;
    }

    /**
     * Return the Id of the entity
     * @return Id of the entity
     */
    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Entity)) return false;

        Entity other = (Entity) obj;

        return getId() == other.getId();
    }

    @Override
    public String toString() {
        return "Entity{" +
                "position=" + position +
                ", visible=" + visible +
                ", sprite=" + sprite +
                ", id=" + id +
                '}';
    }
}
