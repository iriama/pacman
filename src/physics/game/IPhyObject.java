package physics.game;

import geometry.Point;
import geometry.Rect;

public interface IPhyObject {
    int getId();

    Point getPosition();

    void setPosition(Point position);

    int getX();

    int getY();

    void setPosition(int x, int y);

    void setVelocity(int x, int y);

    Point getVelocity();

    void setVelocity(Point vector);

    int getVelocityX();

    void setVelocityX(int x);

    int getVelocityY();

    void setVelocityY(int y);

    Rect getHitbox();

    void setHitbox(int x, int width, int y, int height);

    boolean collideWith(IPhyObject phyObject);
}
