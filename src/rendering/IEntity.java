package rendering;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface IEntity {
    BufferedImage getSprite();

    void setSprite(String path) throws IOException;

    Point getPosition();

    boolean isVisible();

    void setPosition(int X, int Y);
}
