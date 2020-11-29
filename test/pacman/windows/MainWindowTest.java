package pacman.windows;

import framework.rendering.RenderEngine;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MainWindowTest {

    private JFrame window;

    @BeforeEach
    void setUp() {
        window = new MainWindow(new RenderEngine());
    }

    @AfterEach
    void tearDown() {
        window = null;
    }

    @Test
    void build() {
        assertEquals(window.getHeight(), MainWindow.HEIGHT);
        assertEquals(window.getWidth(), MainWindow.WIDTH);
        assertEquals(window.getTitle(), MainWindow.TITLE);
    }
}