package framework.rendering;

import java.awt.*;

/**
 * Render engine interface
 */
public interface IRenderEngine {
    GraphicObject addObject(GraphicObject object);

    void removeObject(GraphicObject object);

    void update();

    void draw(Graphics2D g);
}