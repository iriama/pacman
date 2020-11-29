import core.engine.CoreEngine;
import core.game.Character;
import input.engine.InputEngine;
import input.sources.KeyStateEnum;
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

            if (pacman.getPhyObject().getX() < -10 && pacman.getPhyObject().getVelocityX() < 0) {
                pacman.getPhyObject().setX(MainWindow.WIDTH - 10);
            } else if (pacman.getPhyObject().getX() > MainWindow.WIDTH && pacman.getPhyObject().getVelocityX() > 0) {
                pacman.getPhyObject().setX(-20);
            }

            if (pacman.getPhyObject().getY() < -10 && pacman.getPhyObject().getVelocityY() < 0) {
                pacman.getPhyObject().setY(MainWindow.HEIGHT - 20);
            } else if (pacman.getPhyObject().getY() > MainWindow.HEIGHT - 20 && pacman.getPhyObject().getVelocityY() > 0) {
                pacman.getPhyObject().setY(-10);
            }

            // GHOST
            if (ghost.getPhyObject().getX() < -10 && ghost.getPhyObject().getVelocityX() < 0) {
                ghost.getPhyObject().setX(MainWindow.WIDTH - 10);
            } else if (ghost.getPhyObject().getX() > MainWindow.WIDTH && ghost.getPhyObject().getVelocityX() > 0) {
                ghost.getPhyObject().setX(-20);
            }

            if (ghost.getPhyObject().getY() < -10 && ghost.getPhyObject().getVelocityY() < 0) {
                ghost.getPhyObject().setY(MainWindow.HEIGHT - 20);
            } else if (ghost.getPhyObject().getY() > MainWindow.HEIGHT - 20 && ghost.getPhyObject().getVelocityY() > 0) {
                ghost.getPhyObject().setY(-10);
            }

        });

        coreEngine.addCharacter(pacman);
        coreEngine.addCharacter(ghost);
        coreEngine.run();
    }

}
