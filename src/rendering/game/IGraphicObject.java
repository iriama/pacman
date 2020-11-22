package rendering.game;

import geometry.Point;
import rendering.graphics.Sprite;

public interface IGraphicObject {
    Sprite getSprite();
    void setSprite(Sprite sprite);

    int getId();

    Point getPosition();

    void setPosition(int X, int Y);

    boolean isVisible();

    void show();

    void hide();
}
