package physics.game;

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

    public boolean canMove(Plate plate) {
        switch (direction) {
            case UP:
                if (this.Y < speed)
                    return false;

            case DOWN:
                if ((this.Y + speed + this.size.getHeight()) > plate.getHeight())
                    return false;

            case LEFT:
                if (this.X < speed)
                    return false;

            case RIGHT:
                if ((this.X + speed + this.size.getWidth()) > plate.getWidth())
                    return false;

        }
        return true;
    }

    public void move() {

        switch (direction) {
            case UP:
                this.Y -= speed;

            case DOWN:
                this.Y += speed;

            case LEFT:
                this.X -= speed;

            case RIGHT:
                this.X += speed;
        }
    }

    public boolean isTouch(GameObject object) {
        return (object.getX() < this.getX() + this.size.getWidth()) && (object.getX() + object.size.getWidth() < this.getX())
                && (object.getY() < this.getY() + this.size.getHeight()) && (object.getY() + this.size.getHeight() < this.getY());
    }
}

