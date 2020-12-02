package framework.physics;

import framework.utility.IdFactory;

import java.util.Vector;

public class PhysicsEngine implements IPhysicsEngine {

    private Vector<PhyObject> objects;

    public PhysicsEngine() {
        objects = new Vector<PhyObject>();
    }

    /**
     * Creates an object
     *
     * @param x      object X coord
     * @param width  object width
     * @param y      object Y coord
     * @param height object height
     * @return added object
     */
    public static PhyObject createObject(int x, int width, int y, int height) {
        return new PhyObject(x, width, y, height, IdFactory.nextId());
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
