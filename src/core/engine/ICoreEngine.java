package core.engine;

import core.game.ICharacter;
import physics.game.IPhyObject;
import rendering.game.IGraphicObject;

public interface ICoreEngine {
    ICharacter addCharacter(String spriteSheetPath, int spriteWidth, int spriteCount, int x, int width, int y, int height);

    ICharacter addCharacter(IGraphicObject graphicObject, IPhyObject phyObject);

    void removeCharacter(int characterId);
}
