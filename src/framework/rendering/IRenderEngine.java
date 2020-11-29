package framework.rendering;

public interface IRenderEngine {
    GraphicObject addObject(GraphicObject object);

    void removeObject(GraphicObject object);

    void update();
}