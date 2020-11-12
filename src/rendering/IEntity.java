package rendering;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface IEntity {
    Sprite getSprite();

    Point getPosition();

    boolean isVisible();

    void setPosition(int X, int Y);
}
