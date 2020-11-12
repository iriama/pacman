package rendering;

import java.awt.*;

public interface IRenderEngine {
    void draw(Graphics2D g);

    void addEntity(Entity entity);
}