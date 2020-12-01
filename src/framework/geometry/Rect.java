package framework.geometry;

/**
 * Rectangle
 */
public class Rect {
    private int x;
    private int y;
    private int width;
    private int height;

    public Rect(int x, int width, int y, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rect() {
        this(0, 0, 0, 0);
    }

    /**
     * X position of the rectangle
     *
     * @return x position
     */
    public int getX() {
        return x;
    }


    /**
     * Set new X of the rectangle
     *
     * @param x new x position
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Y position of the rectangle
     *
     * @return y position
     */
    public int getY() {
        return y;
    }


    /**
     * Set new Y of the rectangle
     *
     * @param y new y position
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Height of the rectangle
     *
     * @return height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Set new height of the rectangle
     *
     * @param height new height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Width of the rectangle
     *
     * @return width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Set new width of the rectangle
     *
     * @param width new width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Sets paramaters of rectangle
     *
     * @param x      X coord
     * @param width  width
     * @param y      Y coord
     * @param height height
     */
    public void set(int x, int width, int y, int height) {
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
    }

    /**
     * Calculate if two rectangles intersect
     *
     * @param rect other rectangle
     * @return true if itersect
     */
    public boolean intersect(Rect rect) {

        return
                rect.getX() + rect.getWidth() > getX() &&
                        rect.getY() + rect.getHeight() > getY() &&
                        getX() + getWidth() > rect.getX() &&
                        getY() + getHeight() > rect.getY();
    }

    public Rect extend(int X, int Y) {
        return new Rect(
                getX() + X,
                getWidth(),
                getY() + Y,
                getHeight()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rect rect = (Rect) o;
        return x == rect.x &&
                y == rect.y &&
                width == rect.width &&
                height == rect.height;
    }

    @Override
    public String toString() {
        return "Rect{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
