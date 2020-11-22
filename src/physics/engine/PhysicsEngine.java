package physics.engine;

import core.engine.IEngine;
import core.utility.IdFactory;
import physics.game.IPhyObject;
import physics.game.PhyObject;

import java.util.Vector;

public class PhysicsEngine implements IPhysicsEngine, IEngine {

    private Vector<IPhyObject> objects;

    public PhysicsEngine() {
        objects = new Vector<IPhyObject>();
    }

    /**
     * Adds an object to the physics engine
     *
     * @param object object to add
     * @return added object
     */
    public IPhyObject addObject(IPhyObject object) {
        objects.add(object);
        return object;
    }

    /**
     * Adds an object to the physics engine
     *
     * @param x      object X coord
     * @param width  object width
     * @param y      object Y coord
     * @param height object height
     * @return added object
     */
    public IPhyObject addObject(int x, int width, int y, int height) {
        IPhyObject object = new PhyObject(x, width, y, height, IdFactory.nextId());
        return addObject(object);
    }

    /**
     * Remove an object from the physics engine
     *
     * @param object object to remove
     */
    public void removeObject(IPhyObject object) {
        objects.remove(object);
    }

    /**
     * Remove an object from the physics engine
     *
     * @param objectId object's id to remove
     */
    public void removeObject(int objectId) {
        removeObject(new PhyObject(objectId));
    }

    /**
     * Update the objects within the physics engine
     */
    public void update() {
        for (IPhyObject object : objects) {
            object.setPosition(object.getPosition().extend(object.getVelocity()));
        }
    }
}
