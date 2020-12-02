package framework.geometry;

/**
 * Point X,Y coordinates of objects
 */
public class Point {
    private float X;
    private float Y;

    public Point() {
        this(0, 0);
    }

    public Point(float x, float y) {
        set(x, y);
    }

    /**
     * Returns the X coordinate
     *
     * @return X coordinate
     */
    public int getX() {
        return (int) X;
    }

    /**
     * Sets the X coordinate
     *
     * @param x coordinate
     */
    public void setX(int x) {
        X = x;
    }

    /**
     * Return the Y coordinate
     *
     * @return Y coordinate
     */
    public int getY() {
        return (int) Y;
    }

    /**
     * Return the Y coordinate
     *
     * @param y Y coordinate
     */
    public void setY(int y) {
        Y = y;
    }


    /**
     * Set X and Y of the point
     *
     * @param x x coord
     * @param y y coord
     */
    public void set(float x, float y) {
        X = x;
        Y = y;
    }

    /**
     * Return the squared distance to a given point
     *
     * @param p point
     * @return squared distance
     */
    public int distanceSquared(Point p) {
        return (int) Math.sqrt(Math.pow(getX() - p.getX(), 2) + Math.pow(getY() - p.getY(), 2));
    }

    /**
     * Extend by a vector
     *
     * @param vector vector
     * @return the extended point
     */
    public Point extend(Point vector) {
        return new Point(getX() + vector.getX(), getY() + vector.getY());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return X == point.X &&
                Y == point.Y;
    }

    @Override
    public String toString() {
        return "Point{" +
                "X=" + X +
                ", Y=" + Y +
                '}';
    }
}
