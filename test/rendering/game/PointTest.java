package rendering.game;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rendering.graphics.Sprite;
import rendering.graphics.SpriteSheet;

import static org.junit.jupiter.api.Assertions.*;

class PointTest {

    Point point;

    @BeforeEach
    void setUp() {
        point = new Point();
    }

    @AfterEach
    void tearDown() {
        point = null;
    }

    @Test
    void getX() {
        assertEquals(0, point.getX());
    }

    @Test
    void setX() {
        point.setX(2);
        assertEquals(2, point.getX());
    }

    @Test
    void getY() {
        assertEquals(0, point.getY());
    }

    @Test
    void setY() {
        point.setY(3);
        assertEquals(3, point.getY());
    }
}