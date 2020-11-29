package framework.rendering.utility;

import pacman.utility.FontsEngine;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FontsEngineTest {
    @Test
    void getFontName() {
        FontsEngine.start();
        assertEquals(FontsEngine.getFontName("pacfont"), "PacFont");
    }
}