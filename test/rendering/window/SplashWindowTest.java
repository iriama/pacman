package rendering.window;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

class SplashWindowTest {

    private JFrame window;

    @BeforeEach
    void setUp() {
        window = new SplashWindow();
    }

    @AfterEach
    void tearDown() {
        window = null;
    }

    @Test
    void build() {
        assertEquals(window.getContentPane().getComponentCount(), 1);
    }
}