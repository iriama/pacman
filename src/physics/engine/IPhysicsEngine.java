package physics.engine;

import physics.game.PhyObject;

public interface IPhysicsEngine {
    PhyObject addObject(PhyObject object);

    void removeObject(PhyObject object);

    void update();
}
