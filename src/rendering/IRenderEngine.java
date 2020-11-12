package rendering;

import javax.swing.*;
import java.awt.*;

public interface IRenderEngine {
    void addEntity(Entity entity);
    void addEntity(String spritePath, int spriteWidth, int spriteCount);
    JPanel getPanel();
}