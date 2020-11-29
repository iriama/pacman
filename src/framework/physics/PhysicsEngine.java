package framework.physics;

import framework.utility.IdFactory;

import java.util.Vector;

public class PhysicsEngine implements IPhysicsEngine {

    private Vector<PhyObject> objects;

    public PhysicsEngine() {
        objects = new Vector<PhyObject>();
    }

    /**
     * Adds an object to the framework.physics engine
     *
     * @param object object to add
     * @return added object
     */
    public PhyObject addObject(PhyObject object) {
        objects.add(object);
        return object;
    }

    /**
     * Adds an object to the framework.physics engine
     *
     * @param x      object X coord
     * @param width  object width
     * @param y      object Y coord
     * @param height object height
     * @return added object
     */
    public PhyObject addObject(int x, int width, int y, int height) {
        PhyObject object = new PhyObject(x, width, y, height, IdFactory.nextId());
        return addObject(object);
    }

    /**
     * Remove an object from the framework.physics engine
     *
     * @param object object to remove
     */
    public void removeObject(PhyObject object) {
        objects.remove(object);
    }


    /**
     * Update the objects within the framework.physics engine
     */
    public void update() {
        for (PhyObject object : objects) {
            object.setPosition(object.getPosition().extend(object.getVelocity()));
        }
    }
}
