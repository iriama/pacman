package physics.game;

public class GameObject {

    public int X;
    public int Y;
    public Size size;

    public GameObject(int x, int y, Size size) {
        this.X = x;
        this.Y = y;
        this.size = size;
    }

    public int getX() {
        return this.X;
    }

    public int getY(){
        return this.Y;
    }

    public Size getSize() {
        return size;
    }
}
