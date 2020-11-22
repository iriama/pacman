package rendering.engine;

import geometry.Point;
import rendering.game.GraphicObject;
import rendering.game.IGraphicObject;

public interface IRenderEngine {
    IGraphicObject addObject(IGraphicObject object);

    IGraphicObject addObject(String spriteSheetPath, int spriteWidth, int spriteCount);

    void removeObject(GraphicObject object);

    void removeObject(int objectId);

    Point getCameraPosition();

    void moveCamera(int x, int y);
}