package framework.rendering.utility;

import org.junit.jupiter.api.Test;
import pacman.utility.FontsEngine;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FontsEngineTest {
    @Test
    void getFontName() {
        FontsEngine.start();
        assertEquals(FontsEngine.getFontName("pacfont"), "PacFont");
    }
}