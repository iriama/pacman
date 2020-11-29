package rendering.engine;

import rendering.game.GraphicObject;

public interface IRenderEngine {
    GraphicObject addObject(GraphicObject object);

    void removeObject(GraphicObject object);

    void update();
}