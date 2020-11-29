package framework.physics.engine;

import framework.physics.PhyObject;
import framework.physics.PhysicsEngine;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

}