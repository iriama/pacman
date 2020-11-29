package framework.physics;

public interface IPhysicsEngine {
    PhyObject addObject(PhyObject object);

    void removeObject(PhyObject object);

    void update();
}
