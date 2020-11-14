package rendering.game;

import rendering.graphics.Sprite;

public interface IEntity {
    Sprite getSprite();

    Point getPosition();

    boolean isVisible();

    void setPosition(int X, int Y);

    void show();

    void hide();
}
