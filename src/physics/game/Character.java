package physics.game;

import java.util.Vector;

enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

public class Character extends GameObject {

    private int speed;
    private Direction direction;

    public Character(int x, int y, Size size, int speed, Direction direction){
        super(x, y, size);
        this.speed = speed;
        this.direction = direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Direction getDirection() {
        return direction;
    }

    public double getSpeed() {
        return speed;
    }

    public boolean canMove(Direction direction, int speed, Plate plate) {
        switch (direction) {
            case Direction.UP:
                if (this.Y < speed)
                    return false;

            case Direction.DOWN:
                if ((this.Y + speed) > plate.getHeight())
                    return false;

            case Direction.LEFT:
                if (this.X < speed)
                    return false;

            case Direction.RIGHT:
                if ((this.X + speed) > plate.getWidth())
                    return false;

        }
        return true;
    }

    public void move(Direction direction, int speed){

            switch(direction){
                case Direction.UP:
                    this.Y -= speed;

                case Direction.DOWN:
                    this.Y += speed;

                case Direction.LEFT:
                    this.X -= speed;

                case Direction.RIGHT:
                    this.X += speed;
            }
    }
}

public  class Plate{

    private int width;
    private int height;

    public Plate(int w, int h){
        this.width = w;
        this.height = h;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}

/*public class simulMovement{
    public static void main(String []args){
        Plate p = new Plate(300,600);
        GameObject obj = new Character(0, 0, 2, Direction.DOWN);
        while(obj.canMove(obj.getDirection, obj.getSpeed, p)){
            obj.move(obj.getDirection, obj.getSpeed);
            System.out.println("new X : "+obj.getX+" new Y : "+obj.getY);
            Thread.sleep(200);
        }
    }
}*/

public class PhysEngine{
    private Vector<GameObject> characters;
}
