package physics.game;

import java.util.Vector;

public class PhysicsEngine {
    private final Vector<Character> characters;
    private Vector<GameObject> objects;
    private final Plate plate;

    public PhysicsEngine(Plate plate) {
        this.characters = new Vector<Character>();
        this.plate = plate;
    }

    public void addObject(GameObject object) {
        this.objects.add(object);
    }

    public void removeObject(GameObject object) {
        this.objects.remove(object);
    }

    public void addCharacter(Character character) {
        this.characters.add(character);
        addObject(character);
    }

    public void removeCharacter(Character character) {
        this.characters.remove(character);
        removeObject(character);
    }

    public void move() {
        for (Character c : characters)
            if (c.canMove(this.plate))
                move();
    }

    public Vector<PairGameObject> checkCollision() {
        Vector<PairGameObject> objectsInCollision = new Vector<PairGameObject>();
        for (Character c : characters)
            for (GameObject o : this.objects) {
                if (c.equals(o)) continue;
                if (c.isTouch(o)) objectsInCollision.add(new PairGameObject(c, o));
            }

        return objectsInCollision;
    }
}
