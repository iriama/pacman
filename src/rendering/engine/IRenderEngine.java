package rendering.engine;

import geometry.Point;
import rendering.game.Entity;
import rendering.game.IEntity;

public interface IRenderEngine {
    IEntity addEntity(IEntity entity);

    IEntity addEntity(String spriteSheetPath, int spriteWidth, int spriteCount);

    void removeEntity(Entity entity);

    void removeEntity(int index);

    Point getCameraPosition();

    void moveCamera(int x, int y);
}