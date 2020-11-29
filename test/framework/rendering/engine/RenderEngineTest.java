package framework.rendering.engine;

import framework.rendering.GraphicObject;
import framework.rendering.RenderEngine;
import framework.rendering.graphics.Sprite;
import framework.rendering.graphics.SpriteSheet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RenderEngineTest {

    JPanel panel;
    RenderEngine engine;

    @BeforeEach
    void setUp() {
        panel = new JPanel();
        engine = new RenderEngine(() -> panel.repaint());
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
}