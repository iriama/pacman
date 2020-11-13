package rendering.utility;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Fonts manager
 */
public class FontsEngine {
    private static final HashMap<String, String> fontsName = new HashMap<String, String>();

    /**
     * Start the font manager and register fonts
     */
    public static void start() {
        try {
            registerFont("ressources/fonts/pacfont.ttf");
        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }
    }

    private static void registerFont(String path) throws IOException, FontFormatException {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        File file = new File(path);

        if (!file.exists() || file.isDirectory())
            throw new IOException("font path is incorrect");

        String id = file.getName().toLowerCase().split("\\.")[0].toLowerCase();
        Font font = Font.createFont(Font.TRUETYPE_FONT, file);

        ge.registerFont(font);
        fontsName.put(id, font.getName());
    }

    /**
     * Returns font name by id
     *
     * @param id
     * @return font name
     */
    public static String getFontName(String id) {
        return fontsName.get(id);
    }
}
