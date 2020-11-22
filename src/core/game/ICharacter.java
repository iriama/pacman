package core.game;

import physics.game.IPhyObject;
import rendering.game.IGraphicObject;

public interface ICharacter {
    IGraphicObject getGraphicObject();

    IPhyObject getPhyObject();

    int getId();
}
