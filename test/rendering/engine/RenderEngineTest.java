package rendering.engine;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rendering.game.GraphicObject;
import rendering.game.IGraphicObject;
import rendering.graphics.Sprite;
import rendering.graphics.SpriteSheet;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RenderEngineTest {

    RenderEngine engine;

    @BeforeEach
    void setUp() {
        engine = new RenderEngine();
    }

    @AfterEach
    void tearDown() {
        engine = null;
    }

    @Test
    void camera() {
        assertEquals(0, engine.getCameraPosition().getX());
        assertEquals(0, engine.getCameraPosition().getY());
        engine.moveCamera(100, 200);
        assertEquals(100, engine.getCameraPosition().getX());
        assertEquals(200, engine.getCameraPosition().getY());
    }

    @Test
    void entity() {

        IGraphicObject entity = new GraphicObject(new Sprite(
                new SpriteSheet("ressources/sprites/test.png", 128, 4)
        ), 1337);

        IGraphicObject res = engine.addObject(entity);
        assertEquals(1337, res.getId());
        res = engine.addObject("ressources/sprites/test.png", 128, 4);
        assertEquals(Integer.MIN_VALUE + 1, res.getId());

    }
}