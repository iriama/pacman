package physics.engine;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import physics.game.IPhyObject;
import physics.game.PhyObject;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PhysicsEngineTest {

    PhysicsEngine engine;

    @BeforeEach
    void setUp() {
        engine = new PhysicsEngine();
    }

    @AfterEach
    void tearDown() {
        engine = null;
    }

    @Test
    void addObject() {
        IPhyObject cmp = new PhyObject(0, 100, 20, 100, Integer.MIN_VALUE + 1);
        IPhyObject object = engine.addObject(0, 100, 20, 100);
        assertTrue(cmp.equals(object));
    }
}