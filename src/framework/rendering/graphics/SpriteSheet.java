package framework.rendering.graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Sprite sheet with multiple images in a 1-Dimension spreadsheet
 */
public class SpriteSheet {

    private final String path;
    private final int spriteCount;
    private final int spriteWidth;
    private final int spriteHeight;
    private BufferedImage image = null;

    public SpriteSheet(String path, int spriteWidth) throws IOException {
        this.path = path;
        loadToMemory();
        this.spriteCount = image.getWidth() / spriteWidth;
        this.spriteWidth = spriteWidth;

        spriteHeight = image.getHeight();
    }

    private void loadToMemory() throws IOException {
        if (image != null) return;

        image = ImageIO.read(new File(path));
    }

    /**
     * Return the number of sprites (images) on the sprite sheet
     *
     * @return number of sprites on the sprite sheet
     */
    public int getSpriteCount() {
        return spriteCount;
    }

    /**
     * Return a sprite (image) on the sprite sheet
     *
     * @param index index of the sprite (image)
     * @return Image
     */
    public BufferedImage getSpriteImage(int index) {
        return image.getSubimage(index * spriteWidth, 0, spriteWidth, spriteHeight);
    }
}
