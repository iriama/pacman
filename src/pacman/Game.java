package pacman;

import framework.core.Character;
import framework.core.CoreEngine;
import framework.physics.PhyObject;
import framework.physics.PhysicsEngine;
import framework.rendering.GraphicObject;
import framework.rendering.IPanel;
import framework.rendering.RenderEngine;
import framework.rendering.graphics.Sprite;
import framework.rendering.graphics.SpriteSheet;
import javafx.application.Application;
import pacman.windows.MainWindow;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class Game extends JPanel implements IPanel {

    Vector<BufferedImage> maps;

    private RenderEngine renderEngine;
    private PhysicsEngine physicsEngine;
    private CoreEngine coreEngine;

    public Game() {
        super();

        setBackground(Color.black);

        try {
            loadAssets();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void setRenderEngine(RenderEngine renderEngine) {
        this.renderEngine = renderEngine;
    }

    public void setPhysicsEngine(PhysicsEngine physicsEngine) {
        this.physicsEngine = physicsEngine;
    }

    public void setCoreEngine(CoreEngine coreEngine) {
        this.coreEngine = coreEngine;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Background
        g.drawImage(maps.get(0), 0, 0, 450, 500, null);

        renderEngine.draw((Graphics2D)g);
    }

    private void loadAssets() throws IOException {
        // Maps
        maps = new Vector<BufferedImage>();
        maps.add(
                ImageIO.read(new File("ressources/maps/map_1.png"))
        );
    }
}
