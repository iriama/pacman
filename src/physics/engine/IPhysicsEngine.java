package physics.engine;

import physics.game.IPhyObject;

public interface IPhysicsEngine {
    IPhyObject addObject(IPhyObject object);

    IPhyObject addObject(int x, int width, int y, int height);

    void removeObject(IPhyObject object);

    void removeObject(int objectId);
}
