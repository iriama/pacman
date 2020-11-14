package rendering.game;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rendering.graphics.Sprite;
import rendering.graphics.SpriteSheet;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class EntityTest {

    Entity entity;
    Sprite sprite;

    @BeforeEach
    void setUp() {
        sprite = new Sprite(
                new SpriteSheet("ressources/sprites/test.png", 128, 4)
        );

        entity = new Entity(sprite, 1337);
    }

    @AfterEach
    void tearDown() {
        sprite = null;
        entity = null;
    }

    @Test
    void getSprite() throws IOException {
        BufferedImage img1 = sprite.getImage();
        BufferedImage img2 = entity.getSprite().getImage();
        ByteArrayOutputStream img1out = new ByteArrayOutputStream();
        ByteArrayOutputStream img2out = new ByteArrayOutputStream();

        ImageIO.write(img1, "png", img1out);
        ImageIO.write(img2, "png", img2out);

        assertTrue(Arrays.equals(img1out.toByteArray(), img2out.toByteArray()));
    }

    @Test
    void position() {
        assertEquals(new Point(0, 0), entity.getPosition());
        entity.setPosition(2, 2);
        assertEquals(new Point(2, 2), entity.getPosition());
    }

    @Test
    void visibility() {
        assertTrue(entity.isVisible());
        entity.hide();
        assertFalse(entity.isVisible());
        entity.show();
        assertTrue(entity.isVisible());
    }

    @Test
    void getId() {
        assertEquals(1337, entity.getId());
    }

    @Test
    void testEquals() {
        Entity other = new Entity(null, 1337);
        assertEquals(entity, other);
    }
}