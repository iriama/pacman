import core.engine.CoreEngine;
import core.game.Character;
import input.engine.InputEngine;
import input.sources.Keyboard;
import physics.engine.PhysicsEngine;
import physics.game.PhyObject;
import rendering.engine.RenderEngine;
import rendering.game.GraphicObject;
import rendering.graphics.Sprite;
import rendering.graphics.SpriteSheet;
import rendering.utility.FontsEngine;
import rendering.window.MainWindow;
import rendering.window.SplashWindow;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.concurrent.atomic.AtomicBoolean;

public class Entrypoint {

    static RenderEngine renderEngine = new RenderEngine();
    static PhysicsEngine physicsEngine = new PhysicsEngine();
    static InputEngine inputEngine = new InputEngine();
    static CoreEngine coreEngine;


    public static void main(String[] args) throws InterruptedException {
        FontsEngine.start();

        JFrame splash = new SplashWindow();
        JFrame win = new MainWindow(renderEngine);

        SwingUtilities.invokeLater(() -> {
            splash.setVisible(true);
        });

        Thread.sleep(2000);

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
        pacman.getGraphicObject().getSprite().loop(20);
        pacman.getPhyObject().setVelocityX(5);

        SpriteSheet ghost_left = new SpriteSheet("ressources/sprites/ghost/cyan_left.png", 28, 4);
        SpriteSheet ghost_right = new SpriteSheet("ressources/sprites/ghost/cyan_right.png", 28, 4);
        SpriteSheet ghost_down = new SpriteSheet("ressources/sprites/ghost/cyan_down.png", 28, 4);
        SpriteSheet ghost_up = new SpriteSheet("ressources/sprites/ghost/cyan_up.png", 28, 4);

        Character ghost = new Character(new GraphicObject(new Sprite(ghost_left), 1), new PhyObject(0, 28, 0, 28, 1), 1);
        ghost.getGraphicObject().getSprite().loop(40);
        ghost.getPhyObject().setX(MainWindow.WIDTH - 28);
        ghost.getPhyObject().setVelocityX(-5);


        // Z = up ; S = down; Q = left; D = right
        Keyboard pacman_keyboard = new Keyboard((int key, boolean state) -> {
            if (gameOver.get() || !state)
                return;

            switch (key) {
                case KeyEvent.VK_Z:
                    Sprite up = new Sprite(pacman_up);
                    up.loop(20);
                    pacman.getGraphicObject().setSprite(up);
                    pacman.getPhyObject().setVelocity(0, -5);
                    break;
                case KeyEvent.VK_S:
                    Sprite down = new Sprite(pacman_down);
                    down.loop(20);
                    pacman.getGraphicObject().setSprite(down);
                    pacman.getPhyObject().setVelocity(0, 5);
                    break;
                case KeyEvent.VK_Q:
                    Sprite left = new Sprite(pacman_left);
                    left.loop(20);
                    pacman.getGraphicObject().setSprite(left);
                    pacman.getPhyObject().setVelocity(-5, 0);
                    break;
                case KeyEvent.VK_D:
                    Sprite right = new Sprite(pacman_right);
                    right.loop(20);
                    pacman.getGraphicObject().setSprite(right);
                    pacman.getPhyObject().setVelocity(5, 0);
                    break;

            }
        });

        // GHOST
        Keyboard ghost_keyboard = new Keyboard((int key, boolean state) -> {
            if (gameOver.get() || !state)
                return;

            switch (key) {
                case KeyEvent.VK_UP:
                    Sprite up = new Sprite(ghost_up);
                    up.loop(20);
                    ghost.getGraphicObject().setSprite(up);
                    ghost.getPhyObject().setVelocity(0, -5);
                    break;
                case KeyEvent.VK_DOWN:
                    Sprite down = new Sprite(ghost_down);
                    down.loop(20);
                    ghost.getGraphicObject().setSprite(down);
                    ghost.getPhyObject().setVelocity(0, 5);
                    break;
                case KeyEvent.VK_LEFT:
                    Sprite left = new Sprite(ghost_left);
                    left.loop(20);
                    ghost.getGraphicObject().setSprite(left);
                    ghost.getPhyObject().setVelocity(-5, 0);
                    break;
                case KeyEvent.VK_RIGHT:
                    Sprite right = new Sprite(ghost_right);
                    right.loop(20);
                    ghost.getGraphicObject().setSprite(right);
                    ghost.getPhyObject().setVelocity(5, 0);
                    break;

            }
        });

        inputEngine.addSource(pacman_keyboard);
        inputEngine.addSource(ghost_keyboard);


        coreEngine = new CoreEngine(renderEngine, physicsEngine, inputEngine, () -> {
            if (gameOver.get()) return;

            if (pacman.getPhyObject().collideWith(ghost.getPhyObject())) {
                pacman.getPhyObject().setVelocity(0, 0);
                ghost.getPhyObject().setVelocity(0, 0);
                Sprite death = new Sprite(pacman_death);
                death.play(20);
                pacman.getGraphicObject().setSprite(death);
                gameOver.set(true);
            }

            if (pacman.getPhyObject().getX() < -10 && pacman.getPhyObject().getVelocityX() < 0) {
                pacman.getPhyObject().setX(MainWindow.WIDTH - 10);
            }
            else if (pacman.getPhyObject().getX() > MainWindow.WIDTH && pacman.getPhyObject().getVelocityX() > 0) {
                pacman.getPhyObject().setX(-20);
            }

            if (pacman.getPhyObject().getY() < -10 && pacman.getPhyObject().getVelocityY() < 0) {
                pacman.getPhyObject().setY(MainWindow.HEIGHT - 20);
            }
            else if (pacman.getPhyObject().getY() > MainWindow.HEIGHT - 20 && pacman.getPhyObject().getVelocityY() > 0) {
                pacman.getPhyObject().setY(-10);
            }

            // GHOST
            if (ghost.getPhyObject().getX() < -10 && ghost.getPhyObject().getVelocityX() < 0) {
                ghost.getPhyObject().setX(MainWindow.WIDTH - 10);
            }
            else if (ghost.getPhyObject().getX() > MainWindow.WIDTH && ghost.getPhyObject().getVelocityX() > 0) {
                ghost.getPhyObject().setX(-20);
            }

            if (ghost.getPhyObject().getY() < -10 && ghost.getPhyObject().getVelocityY() < 0) {
                ghost.getPhyObject().setY(MainWindow.HEIGHT - 20);
            }
            else if (ghost.getPhyObject().getY() > MainWindow.HEIGHT - 20 && ghost.getPhyObject().getVelocityY() > 0) {
                ghost.getPhyObject().setY(-10);
            }

        });

        coreEngine.addCharacter(pacman);
        coreEngine.addCharacter(ghost);
        coreEngine.run();
    }

}
