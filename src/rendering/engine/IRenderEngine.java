package rendering.engine;

import rendering.game.Entity;

import javax.swing.*;

public interface IRenderEngine {
    Entity addEntity(Entity entity);

    Entity addEntity(String spriteSheetPath, int spriteWidth, int spriteCount);

    void removeEntity(Entity entity);

    void removeEntity(int entityId);

    JPanel getPanel();
}