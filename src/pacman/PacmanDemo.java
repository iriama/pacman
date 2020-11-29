package pacman;

import framework.core.Character;
import framework.core.CoreEngine;
import framework.geometry.Rect;
import framework.input.InputEngine;
import framework.input.sources.KeyStateEnum;
import framework.input.sources.Keyboard;
import framework.physics.PhyObject;
import framework.physics.PhysicsEngine;
import framework.rendering.GraphicObject;
import framework.rendering.RenderEngine;
import framework.rendering.graphics.Sprite;
import framework.rendering.graphics.SpriteSheet;
import pacman.utility.FontsEngine;
import pacman.windows.MainWindow;
import pacman.windows.SplashWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.concurrent.atomic.AtomicBoolean;

public class PacmanDemo {

    private static Character _pacman = null;
    private static Character _ghost = null;

    public static JPanel panel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            renderEngine.draw((Graphics2D) g);

            // DEBUG
            /*
            g.setColor(Color.green);
            g.drawRect(renderEngine.getCameraPosition().getX(), renderEngine.getCameraPosition().getY(), MainWindow.WIDTH, MainWindow.HEIGHT);

            if (_pacman != null) {
                Rect hitbox = _pacman.getPhyObject().getHitbox();
                g.setColor(Color.green);
                g.drawRect(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());

                Rect ghitbox = _ghost.getPhyObject().getHitbox();
                g.setColor(Color.red);
                g.drawRect(ghitbox.getX(), ghitbox.getY(), ghitbox.getWidth(), ghitbox.getHeight());
            }*/

        }
    };
    static RenderEngine renderEngine = new RenderEngine(() -> panel.repaint());
    static PhysicsEngine physicsEngine = new PhysicsEngine();
    static InputEngine inputEngine = new InputEngine();
    static CoreEngine coreEngine;


    public static void main(String[] args) throws InterruptedException {

        FontsEngine.start();

        SplashWindow splash = new SplashWindow();
        MainWindow win = new MainWindow();
        panel.setBackground(Color.black);
        win.setPanel(panel);

        SwingUtilities.invokeLater(() -> {
            splash.setVisible(true);
        });

        Thread.sleep(1000);

        SwingUtilities.invokeLater(() -> {
            splash.setVisible(false);
            win.setVisible(true);
        });

        // Demo
        demo();
    }

    static void demo() {

        SpriteSheet pacman_left = new SpriteSheet("ressources/sprites/pacman/pacman_left.png", 28, 10);
        SpriteSheet pacman_right = new SpriteSheet("ressources/sprites/pacman/pacman_right.png", 28, 10);
        SpriteSheet pacman_down = new SpriteSheet("ressources/sprites/pacman/pacman_down.png", 28, 10);
        SpriteSheet pacman_up = new SpriteSheet("ressources/sprites/pacman/pacman_up.png", 28, 10);
        SpriteSheet pacman_death = new SpriteSheet("ressources/sprites/pacman/pacman_death.png", 28, 17);
        AtomicBoolean gameOver = new AtomicBoolean(false);


        Character pacman = new Character(new GraphicObject(new Sprite(pacman_right), 0), new PhyObject(0, 28, 0, 28, 0), 0);
        _pacman = pacman;
        pacman.getGraphicObject().getSprite().loop(20);
        pacman.getPhyObject().setVelocityX(5);

        SpriteSheet ghost_left = new SpriteSheet("ressources/sprites/ghost/cyan_left.png", 28, 4);
        SpriteSheet ghost_right = new SpriteSheet("ressources/sprites/ghost/cyan_right.png", 28, 4);
        SpriteSheet ghost_down = new SpriteSheet("ressources/sprites/ghost/cyan_down.png", 28, 4);
        SpriteSheet ghost_up = new SpriteSheet("ressources/sprites/ghost/cyan_up.png", 28, 4);

        Character ghost = new Character(new GraphicObject(new Sprite(ghost_left), 1), new PhyObject(MainWindow.WIDTH, 28, 30, 28, 1), 1);
        _ghost = ghost;
        ghost.getGraphicObject().getSprite().loop(40);
        ghost.getPhyObject().setVelocityX(-5);


        Keyboard pacman_keyboard = new Keyboard();

        pacman_keyboard.mapKey(KeyEvent.VK_Z, KeyStateEnum.PRESSED, () -> {
            pacman.getGraphicObject().setSpriteSheet(pacman_up);
            pacman.getPhyObject().setVelocity(0, -5);
        });

        pacman_keyboard.mapKey(KeyEvent.VK_S, KeyStateEnum.PRESSED, () -> {
            pacman.getGraphicObject().setSpriteSheet(pacman_down);
            pacman.getPhyObject().setVelocity(0, 5);
        });

        pacman_keyboard.mapKey(KeyEvent.VK_Q, KeyStateEnum.PRESSED, () -> {
            pacman.getGraphicObject().setSpriteSheet(pacman_left);
            pacman.getPhyObject().setVelocity(-5, 0);
        });

        pacman_keyboard.mapKey(KeyEvent.VK_D, KeyStateEnum.PRESSED, () -> {
            pacman.getGraphicObject().setSpriteSheet(pacman_right);
            pacman.getPhyObject().setVelocity(5, 0);
        });


        Keyboard ghost_keyboard = new Keyboard();

        ghost_keyboard.mapKey(KeyEvent.VK_UP, KeyStateEnum.PRESSED, () -> {
            ghost.getGraphicObject().setSpriteSheet(ghost_up);
            ghost.getPhyObject().setVelocity(0, -5);
        });

        ghost_keyboard.mapKey(KeyEvent.VK_DOWN, KeyStateEnum.PRESSED, () -> {
            ghost.getGraphicObject().setSpriteSheet(ghost_down);
            ghost.getPhyObject().setVelocity(0, 5);
        });

        ghost_keyboard.mapKey(KeyEvent.VK_LEFT, KeyStateEnum.PRESSED, () -> {
            ghost.getGraphicObject().setSpriteSheet(ghost_left);
            ghost.getPhyObject().setVelocity(-5, 0);
        });

        ghost_keyboard.mapKey(KeyEvent.VK_RIGHT, KeyStateEnum.PRESSED, () -> {
            ghost.getGraphicObject().setSpriteSheet(ghost_right);
            ghost.getPhyObject().setVelocity(5, 0);
        });

        inputEngine.addSource(pacman_keyboard);
        inputEngine.addSource(ghost_keyboard);


        coreEngine = new CoreEngine(renderEngine, physicsEngine, inputEngine, () -> {
            if (gameOver.get()) return;

            if (pacman.getPhyObject().collideWith(ghost.getPhyObject())) {

                pacman_keyboard.disable();
                ghost_keyboard.disable();

                pacman.getPhyObject().setVelocity(0, 0);
                ghost.getPhyObject().setVelocity(0, 0);
                Sprite death = new Sprite(pacman_death);
                death.play(20);
                death.onPlayFinish(() -> coreEngine.removeCharacter(pacman));
                pacman.getGraphicObject().setSprite(death);
                gameOver.set(true);
            }

            if (pacman.getPhyObject().getX() <= -28 && pacman.getPhyObject().getVelocityX() < 0) {
                pacman.getPhyObject().setX(MainWindow.WIDTH);
            } else if (pacman.getPhyObject().getX() >= MainWindow.WIDTH && pacman.getPhyObject().getVelocityX() > 0) {
                pacman.getPhyObject().setX(-28);
            }

            if (pacman.getPhyObject().getY() <= -28 && pacman.getPhyObject().getVelocityY() < 0) {
                pacman.getPhyObject().setY(MainWindow.HEIGHT);
            } else if (pacman.getPhyObject().getY() >= MainWindow.HEIGHT && pacman.getPhyObject().getVelocityY() > 0) {
                pacman.getPhyObject().setY(-28);
            }

            // GHOST
            if (ghost.getPhyObject().getX() <= -28 && ghost.getPhyObject().getVelocityX() < 0) {
                ghost.getPhyObject().setX(MainWindow.WIDTH);
            } else if (ghost.getPhyObject().getX() >= MainWindow.WIDTH && ghost.getPhyObject().getVelocityX() > 0) {
                ghost.getPhyObject().setX(-28);
            }

            if (ghost.getPhyObject().getY() <= -28 && ghost.getPhyObject().getVelocityY() < 0) {
                ghost.getPhyObject().setY(MainWindow.HEIGHT);
            } else if (ghost.getPhyObject().getY() >= MainWindow.HEIGHT - 5 && ghost.getPhyObject().getVelocityY() > 0) {
                ghost.getPhyObject().setY(-28);
            }

        });

        coreEngine.addCharacter(pacman);
        coreEngine.addCharacter(ghost);
        coreEngine.run();
    }

}
