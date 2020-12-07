package framework.physics;

/**
 * Physics engine interface
 */
public interface IPhysicsEngine {
    PhyObject addObject(PhyObject object);

    void removeObject(PhyObject object);

    void update();
}
