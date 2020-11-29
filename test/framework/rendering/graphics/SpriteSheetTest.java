package framework.rendering.graphics;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class SpriteSheetTest {

    SpriteSheet sheet;

    @BeforeEach
    void setUp() {
        sheet = new SpriteSheet("ressources/sprites/test.png", 128, 4);
    }

    @AfterEach
    void tearDown() {
        sheet = null;
    }

    @Test
    void getSpriteCount() {
        assertEquals(sheet.getSpriteCount(), 4);
    }

    @Test
    void getSpriteImage() throws IOException {
        BufferedImage img1 = sheet.getSpriteImage(0);
        BufferedImage img2 = sheet.getSpriteImage(1);
        ByteArrayOutputStream img1out = new ByteArrayOutputStream();
        ByteArrayOutputStream img2out = new ByteArrayOutputStream();

        ImageIO.write(img1, "png", img1out);
        ImageIO.write(img2, "png", img2out);

        assertFalse(Arrays.equals(img1out.toByteArray(), img2out.toByteArray()));
    }
}