package physics.game;

public class GameObject {

    private int X;
    private int Y;
    private Size size''

    public  GameObject(int x, int y, Size size){
        this.X = x;
        this.Y = y;
        this.size = size;
    }

    public int getX(){
        return this.X;
    }

    public int getY(){
        return this.Y;
    }

    public Size getSize() {
        return size;
    }
}
 public class Size{
    private int width;
    private int height;

    public Size(int w, int h){
        this.height = h;
        this.width = w;
    }

     public int getHeight() {
         return height;
     }

     public int getWidth() {
         return width;
     }
 }
