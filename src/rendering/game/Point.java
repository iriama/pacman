package rendering.game;

/**
 * Point X,Y coordinates of objects
 */
public class Point {
    private int X;
    private int Y;

    public Point() {
        this(0, 0);
    }

    public Point(int x, int y) {
        X = x;
        Y = y;
    }

    /**
     * Returns the X coordinate
     * @return X coordinate
     */
    public int getX() {
        return X;
    }

    /**
     * Sets the X coordinate
     * @param x coordinate
     */
    public void setX(int x) {
        X = x;
    }

    /**
     * Return the Y coordinate
     * @return Y coordinate
     */
    public int getY() {
        return Y;
    }

    /**
     * Return the Y coordinate
     * @param y Y coordinate
     */
    public void setY(int y) {
        Y = y;
    }
}
