package framework.rendering.game;

import framework.geometry.Point;
import framework.rendering.GraphicObject;
import framework.rendering.graphics.Sprite;
import framework.rendering.graphics.SpriteSheet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class GraphicObjectTest {

    framework.rendering.GraphicObject GraphicObject;
    Sprite sprite;

    @BeforeEach
    void setUp() {
        sprite = new Sprite(
                new SpriteSheet("ressources/sprites/test.png", 128, 4)
        );

        GraphicObject = new GraphicObject(sprite, 1337);
    }

    @AfterEach
    void tearDown() {
        sprite = null;
        GraphicObject = null;
    }

    @Test
    void getSprite() throws IOException {
        BufferedImage img1 = sprite.getImage();
        BufferedImage img2 = GraphicObject.getSprite().getImage();
        ByteArrayOutputStream img1out = new ByteArrayOutputStream();
        ByteArrayOutputStream img2out = new ByteArrayOutputStream();

        ImageIO.write(img1, "png", img1out);
        ImageIO.write(img2, "png", img2out);

        assertTrue(Arrays.equals(img1out.toByteArray(), img2out.toByteArray()));
    }

    @Test
    void position() {
        assertEquals(new Point(0, 0), GraphicObject.getPosition());
        GraphicObject.setPosition(2, 2);
        assertEquals(new Point(2, 2), GraphicObject.getPosition());
    }

    @Test
    void visibility() {
        assertTrue(GraphicObject.isVisible());
        GraphicObject.hide();
        assertFalse(GraphicObject.isVisible());
        GraphicObject.show();
        assertTrue(GraphicObject.isVisible());
    }

    @Test
    void getId() {
        assertEquals(1337, GraphicObject.getId());
    }

    @Test
    void testEquals() {
        GraphicObject other = new GraphicObject(null, 1337);
        assertEquals(GraphicObject, other);
    }
}