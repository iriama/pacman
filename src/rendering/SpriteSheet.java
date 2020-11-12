package rendering;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SpriteSheet {

    private final String path;
    private BufferedImage image = null;
    private final int spriteCount;
    private final int spriteWidth;

    public SpriteSheet(String path, int spriteCount, int spriteWidth) {
        this.path = path;
        this.spriteCount = spriteCount;
        this.spriteWidth = spriteWidth;

        try {
            loadToMemory();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void loadToMemory() throws IOException {
        if (image != null) return;

        image = ImageIO.read(new File(path));
    }

    public int getSpriteCount() {
        return spriteCount;
    }

    public int getSpriteWidth() {
        return spriteWidth;
    }

    public BufferedImage getImage() {
        return image;
    }
}
