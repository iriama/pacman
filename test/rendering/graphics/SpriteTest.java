package rendering.graphics;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpriteTest {

    Sprite sprite;

    @BeforeEach
    void setUp() {
        sprite = new Sprite(
                new SpriteSheet("ressources/sprites/test.png", 128, 4)
        );
    }

    @AfterEach
    void tearDown() {
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
        for (int i = 0; i<10; i++) sprite.getImage();
        assertEquals(3, sprite.getCurrentFrame());

        sprite.play(1, 2, 0);
        assertEquals(1, sprite.getCurrentFrame());
        for (int i = 0; i<10; i++)  sprite.getImage();
        assertEquals(2, sprite.getCurrentFrame());

        sprite.play(new int[]{2, 0, 1}, 0);
        assertEquals(2, sprite.getCurrentFrame());
        for (int i = 0; i<10; i++)  sprite.getImage();
        assertEquals(1, sprite.getCurrentFrame());
    }

    @Test
    void loop() {
    }

    @Test
    void getImage() {
    }


}