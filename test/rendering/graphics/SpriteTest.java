package rendering.graphics;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SpriteTest {

    SpriteSheet sheet;
    Sprite sprite;

    @BeforeEach
    void setUp() {
        sheet = new SpriteSheet("ressources/sprites/test.png", 128, 4);
        sprite = new Sprite(sheet);
    }

    @AfterEach
    void tearDown() {
        sheet = null;
        sprite = null;
    }

    @Test
    void getCurrentFrame() {
        assertEquals(0, sprite.getCurrentFrame());
    }

    @Test
    void setFrame() {
        sprite.setFrame(2);
        assertEquals(2, sprite.getCurrentFrame());
        sprite.setFrame(1);
        assertEquals(1, sprite.getCurrentFrame());
    }

    @Test
    void play() throws InterruptedException {
        sprite.play(0);
        for (int i = 0; i < 10; i++) sprite.getImage();
        assertEquals(3, sprite.getCurrentFrame());

        sprite.play(1, 2, 0);
        assertEquals(1, sprite.getCurrentFrame());
        for (int i = 0; i < 10; i++) sprite.getImage();
        assertEquals(2, sprite.getCurrentFrame());

        sprite.play(new int[]{2, 0, 1}, 0);
        assertEquals(2, sprite.getCurrentFrame());
        for (int i = 0; i < 10; i++) sprite.getImage();
        assertEquals(1, sprite.getCurrentFrame());
    }

    @Test
    void loop() {
        sprite.loop(0);
        for (int i = 0; i < 2; i++) sprite.getImage();
        assertEquals(2, sprite.getCurrentFrame());

        sprite.loop(1, 2, 0);
        assertEquals(1, sprite.getCurrentFrame());
        for (int i = 0; i < 2; i++) sprite.getImage();
        assertEquals(1, sprite.getCurrentFrame());

        sprite.loop(new int[]{2, 0, 1}, 0);
        assertEquals(2, sprite.getCurrentFrame());
        for (int i = 0; i < 3; i++) sprite.getImage();
        assertEquals(2, sprite.getCurrentFrame());
    }

    @Test
    void getImage() throws IOException {
        BufferedImage imgSprite = sprite.getImage();
        BufferedImage imgSheet = sheet.getSpriteImage(sprite.getCurrentFrame());
        ByteArrayOutputStream imgSpriteO = new ByteArrayOutputStream();
        ByteArrayOutputStream imgSheetO = new ByteArrayOutputStream();

        ImageIO.write(imgSprite, "png", imgSpriteO);
        ImageIO.write(imgSheet, "png", imgSheetO);

        assertTrue(Arrays.equals(imgSheetO.toByteArray(), imgSpriteO.toByteArray()));
    }


    @Test
    void orientationX() {
        assertEquals("INITIAL", sprite.getOrientationX().name());
        sprite.flipX();
        assertEquals("FLIP", sprite.getOrientationX().name());
    }

    @Test
    void orientationY() {
        assertEquals("INITIAL", sprite.getOrientationY().name());
        sprite.flipY();
        assertEquals("FLIP", sprite.getOrientationY().name());
    }

    @Test
    void scale() {
        assertEquals(1, sprite.getScale());
        sprite.setScale(0.8f);
        assertEquals(0.8f, sprite.getScale());
    }

    @Test
    void rotate() {
        assertEquals(0, sprite.getOrientationAngle());
        sprite.rotate(90f);
        assertEquals(90f, sprite.getOrientationAngle());
    }
}