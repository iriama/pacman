package pacman.testing;

import framework.IGameEngine;
import framework.core.Character;
import framework.core.CoreEngine;
import framework.geometry.Rect;
import framework.input.InputEngine;
import framework.input.sources.KeyStateEnum;
import framework.input.sources.Keyboard;
import framework.physics.PhyObject;
import framework.physics.PhysicsEngine;
import framework.rendering.GraphicObject;
import framework.rendering.IPanel;
import framework.rendering.RenderEngine;
import framework.rendering.graphics.SpriteSheet;
import pacman.Map;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Vector;

public class Game extends JPanel implements IPanel, IGameEngine {

    Vector<Map> maps;
    Map currentMap;

    SpriteSheet pacman_left;
    SpriteSheet pacman_right;
    SpriteSheet pacman_down;
    SpriteSheet pacman_up;
    SpriteSheet pacman_death;

    private RenderEngine renderEngine;
    private PhysicsEngine physicsEngine;
    private InputEngine inputEngine;
    private CoreEngine coreEngine;

    private Character pacman;
    int speed = 2;

    public Game() {
        super();

        maps = new Vector<>();
        currentMap = null;

        renderEngine = new RenderEngine(this);
        physicsEngine = new PhysicsEngine();
        inputEngine = new InputEngine();
        coreEngine = new CoreEngine(renderEngine, physicsEngine, inputEngine, this);
    }

    public void start() {

        // 1-time configuration
        setBackground(Color.black);

        try {
            loadAssets();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        GraphicObject pacg = RenderEngine.createObject("ressources/sprites/pacman/pacman_right.png", 28, 10);

        PhyObject pacp = PhysicsEngine.createObject(8, 32, 8, 32);

        pacman = coreEngine.addCharacter(pacg, pacp);

        queue = Directions.RIGHT;

        Keyboard pacboard = new Keyboard();
        pacboard.mapKey(KeyEvent.VK_DOWN, KeyStateEnum.PRESSED, () -> dir(Directions.DOWN));

        pacboard.mapKey(KeyEvent.VK_UP, KeyStateEnum.PRESSED, () -> dir(Directions.UP));

        pacboard.mapKey(KeyEvent.VK_RIGHT, KeyStateEnum.PRESSED, () -> dir(Directions.RIGHT));

        pacboard.mapKey(KeyEvent.VK_LEFT, KeyStateEnum.PRESSED, () -> dir(Directions.LEFT));

        currentMap = maps.get(0);

        inputEngine.addSource(pacboard);
        coreEngine.run();
    }

    enum Directions {
        NONE,
        UP,
        DOWN,
        RIGHT,
        LEFT
    }

    Directions queue = Directions.NONE;



    void dir( Directions dir ) {
        int modX = dir == Directions.LEFT ? -speed : dir == Directions.RIGHT ? speed : 0;
        int modY = dir == Directions.UP ? -speed : dir == Directions.DOWN ? speed : 0;

        PhyObject pacp = pacman.getPhyObject();
        Rect pacHitbox = pacp.getHitbox();
        Rect pacNextHitbox = new Rect(
                pacHitbox.getX() + modX,
                pacHitbox.getWidth(),
                pacHitbox.getY() + modY,
                pacHitbox.getHeight()
        );

        for (Rect wall: currentMap.walls) {
            if (pacNextHitbox.intersect(wall)) {
                queue = dir;
                return;
            }
        }

        queue = Directions.NONE;
        pacp.setVelocity(modX, modY);

        switch (dir) {
            case UP:
                pacman.getGraphicObject().getSprite().setSpriteSheet(pacman_up);
                break;
            case DOWN:
                pacman.getGraphicObject().getSprite().setSpriteSheet(pacman_down);
                break;
            case RIGHT:
                pacman.getGraphicObject().getSprite().setSpriteSheet(pacman_right);
                break;
            case LEFT:
                pacman.getGraphicObject().getSprite().setSpriteSheet(pacman_left);
                break;
        }

        if (pacman.getGraphicObject().getSprite().isPaused()) {
            pacman.getGraphicObject().getSprite().loop(20);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (currentMap == null) return;

        // Map
        g.drawImage(currentMap.image, 0, 0, null);

        // Game
        renderEngine.draw((Graphics2D)g);
    }

    private void loadAssets() throws Exception {
        maps.add(new Map("ressources/maps/map_1.pacmaze"));
        pacman_left = new SpriteSheet("ressources/sprites/pacman/pacman_left.png", 28, 10);
        pacman_right = new SpriteSheet("ressources/sprites/pacman/pacman_right.png", 28, 10);
        pacman_down = new SpriteSheet("ressources/sprites/pacman/pacman_down.png", 28, 10);
        pacman_up = new SpriteSheet("ressources/sprites/pacman/pacman_up.png", 28, 10);
        pacman_death = new SpriteSheet("ressources/sprites/pacman/pacman_death.png", 28, 17);

    }


    public void update() {
        if (currentMap == null) return;

        if (queue != Directions.NONE) {
            dir(queue);
        }


        PhyObject pacPhy = pacman.getPhyObject();
        GraphicObject pacGraph = pacman.getGraphicObject();

        Rect pacHitbox = pacPhy.getHitbox();
        Rect pacNextHitbox = new Rect(
                pacHitbox.getX() + pacPhy.getVelocityX(),
                pacHitbox.getWidth(),
                pacHitbox.getY() + pacPhy.getVelocityY(),
                pacHitbox.getHeight()
        );

        for (Rect wall: currentMap.walls) {
            if (pacNextHitbox.intersect(wall)) {
                pacPhy.setVelocity(0, 0);
                pacGraph.getSprite().setFrame(3);
                break;
            }
        }

    }
}
