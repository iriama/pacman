package physics.game;

import geometry.Point;
import geometry.Rect;

public class PhyObject implements IPhyObject {
    private Point position;
    private Point velocity;
    private Rect hitbox;
    private int id;

    public PhyObject(int id) {
        position = new Point();
        velocity = new Point();
        hitbox = new Rect();
        this.id = id;
    }

    public PhyObject(int x, int width, int y, int height, int id) {
        this.id = id;
        this.position = new Point(x, y);
        this.hitbox = new Rect(x, width, y, height);
        this.velocity = new Point(0, 0);
    }

    /**
     * Returns the id of the object
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Return the position of the object
     *
     * @return Point
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Sets the position of the object
     *
     * @param position position
     */
    public void setPosition(Point position) {
        setPosition(position.getX(), position.getY());
    }

    /**
     * Return X position
     *
     * @return X position
     */
    public int getX() {
        return position.getX();
    }

    /**
     * Return Y position
     *
     * @return Y position
     */
    public int getY() {
        return position.getY();
    }

    /**
     * Sets the position of the object
     *
     * @param x X coord
     * @param y Y coord
     */
    public void setPosition(int x, int y) {
        position.set(x, y);
    }

    /**
     * Sets the velocity vector of the object
     *
     * @param x X velocity
     * @param y Y velocity
     */
    public void setVelocity(int x, int y) {
        velocity.set(x, y);
    }

    /**
     * Returns the velocity vector of the object
     *
     * @return velocity vector
     */
    public Point getVelocity() {
        return velocity;
    }

    /**
     * Sets the velocity vecotr of the object
     *
     * @param vector Vector
     */
    public void setVelocity(Point vector) {
        velocity = vector;
    }

    /**
     * Returns the velocity X vector of the object
     *
     * @return velocity X
     */
    public int getVelocityX() {
        return velocity.getX();
    }

    /**
     * Sets the velocity X vector of the object
     *
     * @param x X velocity
     */
    public void setVelocityX(int x) {
        velocity.setX(x);
    }

    /**
     * Returns the velocity Y vector of the object
     *
     * @return Y velocity
     */
    public int getVelocityY() {
        return velocity.getY();
    }

    /**
     * Sets the velocity Y vector of the object
     *
     * @param y Y velocity
     */
    public void setVelocityY(int y) {
        velocity.setY(y);
    }

    /**
     * Returns the hitbox rectangle of the object
     *
     * @return hitbox rectangle
     */
    public Rect getHitbox() {
        return hitbox;
    }

    /**
     * Sets the hitbox of the object
     *
     * @param x      X position
     * @param width  hitbox width
     * @param y      Y position
     * @param height hitbox height
     */
    public void setHitbox(int x, int width, int y, int height) {
        hitbox.set(x, width, y, height);
    }

    /**
     * Determines if the object is colliding with another object
     *
     * @param phyObject the other object
     * @return true if collides with
     */
    public boolean collideWith(IPhyObject phyObject) {
        return getHitbox().intersect(phyObject.getHitbox());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhyObject phyObject = (PhyObject) o;
        return id == phyObject.id;
    }
}
