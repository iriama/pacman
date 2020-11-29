package framework.physics.game;

import framework.geometry.Point;
import framework.geometry.Rect;
import framework.physics.PhyObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PhyObjectTest {

    PhyObject object;

    @BeforeEach
    void setUp() {
        object = new PhyObject(0, 100, 50, 100, 1337);
    }

    @AfterEach
    void tearDown() {
        object = null;
    }

    @Test
    void id() {
        assertEquals(1337, object.getId());
    }

    @Test
    void position() {
        assertEquals(0, object.getX());
        assertEquals(50, object.getY());

        object.setPosition(30, 20);
        assertEquals(30, object.getX());
        assertEquals(20, object.getY());
    }

    @Test
    void getVelocity() {
        assertEquals(0, object.getVelocityX());
        assertEquals(0, object.getVelocityY());
        assertTrue(new Point(0, 0).equals(object.getVelocity()));

        object.setVelocityX(30);
        object.setVelocityY(20);
        assertEquals(30, object.getVelocityX());
        assertEquals(20, object.getVelocityY());
    }

    @Test
    void hitbox() {
        Rect hitbox = object.getHitbox();
        assertTrue(hitbox.equals(new Rect(0, 100, 50, 100)));
    }

    @Test
    void collideWith() {
        assertTrue(object.collideWith(object));
    }
}