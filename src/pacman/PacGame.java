package pacman;

import framework.AI.IAIModel;
import framework.IGameEngine;
import framework.core.CoreEngine;
import framework.geometry.Point;
import framework.geometry.Rect;
import framework.physics.PhysicsEngine;
import framework.rendering.GraphicObject;
import framework.rendering.IPanel;
import framework.rendering.RenderEngine;
import framework.rendering.graphics.SpriteSheet;
import pacman.AI.*;
import pacman.utility.FontsEngine;
import pacman.windows.MainWindow;
import pacman.windows.SplashWindow;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

public class PacGame extends JPanel implements IPanel, IGameEngine {
    // --- Statics
    public static final int STEP_SIZE = 8;
    public static final int PLAYER_SIZE = 32;
    public static final int TILE_SIZE = STEP_SIZE * 2;
    final static int SPRITE_WIDTH = 28;
    final static int PAC_SPRITE_COUNT = 10;
    final static int PAC_DEATH_SPRITE_COUNT = 17;
    final static int GHOST_SPRITE_COUNT = 4;
    public static PacGame game;
    static SplashWindow splashWindow;
    static MainWindow mainWindow;
    static boolean DEBUG = false;
    // ---




    private Pacman pacman;
    private Vector<Ghost> ghosts;
    private CoreEngine coreEngine;
    public Map currentMap;
    public Level currentLevel;

    public PacGame() {
        coreEngine = new CoreEngine(this, this);
        setBackground(Color.black);
    }

    public static void main(String[] args) {
        for (String arg : args) {
            if (!arg.startsWith("-")) continue;
            switch (arg) {
                case "-debug":
                    DEBUG = true;
                    break;
            }
        }

        FontsEngine.start();
        splashWindow = new SplashWindow();
        mainWindow = new MainWindow();
        game = new PacGame();

        game.play("test");
    }


    public void load(String levelIdentifier) {
        // Load assets
        try {
            // Level
            currentLevel = MemoryDB.getLevel(levelIdentifier);

            // Map
            currentMap = MemoryDB.getMap(currentLevel.mapIdentifier);

            // Pacman
            pacman = Pacman.createPacman(currentLevel.pacman.skinId, currentLevel.pacman.speed, currentMap.pacmanSpawn, SPRITE_WIDTH, PAC_SPRITE_COUNT);
            coreEngine.addCharacter(pacman.getCharacter());

            // Ghosts
            ghosts = new Vector<>();
            for (Level.Actor p : currentLevel.ghosts) {
                Ghost ghost = Ghost.createGhost(p.skinId, p.typeId, p.speed, currentMap.ghostSpawn, SPRITE_WIDTH, GHOST_SPRITE_COUNT);
                ghosts.add(ghost);
                coreEngine.addCharacter(ghost.getCharacter());
            }

            // Set preset arrows to pacman
            coreEngine.addInputSource(
                    pacman.bindControls(
                            MemoryDB.loadPreset("arrows")
                    )
            );

         //Set AI's based on typeId
            for (Ghost ghost : ghosts) {
                IAIModel model = null;

                if (ghost.getControllerId().equals("player")) continue;
                else if (ghost.getControllerId().equals("clyde")) {
                    model = new ClydeAI(pacman, ghost);
                } else if (ghost.getControllerId().equals("pinky")) {
                    model = new PinkyAI(pacman);
                } else if (ghost.getControllerId().equals("inky")) {
                    innerLoop:
                    for (Ghost g : ghosts) {
                        if (g.getControllerId().equals("blinky")) {
                            model = new InkyAI(pacman, g);
                            break innerLoop;
                        }
                    }
                }

                if (model == null) model = new BlinkyAI(pacman); // defaults to blinky
                ghost.setModel(model);
                coreEngine.addAIController(new GhostController(ghost, model));
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }


    private boolean isPlaying() {
        return currentLevel != null;
    }

    public void play(String levelIdentifier) {
        // Load game
        mainWindow.setVisible(false);
        SwingUtilities.invokeLater(() -> splashWindow.setVisible(true));
        load(levelIdentifier);

        // Hook into window
        mainWindow.setPanel(this, currentMap.width, currentMap.height);
        SwingUtilities.invokeLater(() -> {
            splashWindow.setVisible(false);
            mainWindow.setVisible(true);
        });

        // Run engine
        coreEngine.run();
    }

    private void debugPlayer(Graphics g, Color color, Player player) {
        g.setColor(color);
        Rect hitbox = player.getHitbox();
        g.drawRect(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
        g.setColor(Color.white);
        Rect wallHitbox = player.getWallHitbox();
        g.drawRect(wallHitbox.getX(), wallHitbox.getY(), wallHitbox.getWidth(), wallHitbox.getHeight());
        g.setColor(color);
    }

    private void debugAI(Graphics g, IAIModel ai) {
        if (ai instanceof BlinkyAI) g.setColor(Color.red);
        else if (ai instanceof PinkyAI) g.setColor(Color.pink);
        else if (ai instanceof InkyAI) g.setColor(Color.cyan);
        else if (ai instanceof ClydeAI) g.setColor(Color.orange);

        Point prediction = ai.getPrediction();
        g.fillRect(prediction.getX(), prediction.getY(), PacGame.STEP_SIZE, PacGame.STEP_SIZE);
        g.drawString(ai.getClass().getSimpleName(), prediction.getX(), prediction.getY() - 2);
    }


    private void drawDebug(Graphics g) {
        // Walls hitbox
        g.setColor(Color.red);
        for (Rect wall : currentMap.walls) {
            g.drawRect(wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight());
        }

        // Prison wall
        g.setColor(Color.white);
        g.drawRect(currentMap.prisonWall.getX(), currentMap.prisonWall.getY(), currentMap.prisonWall.getWidth(), currentMap.prisonWall.getHeight());

        // PacGame hitboxes
        debugPlayer(g, Color.green, pacman);

        // Ghosts hitboxes & IA
        for (Ghost ghost : ghosts) {
            debugPlayer(g, Color.yellow, ghost);
            if (ghost.getModel() != null)
                debugAI(g, ghost.getModel());
        }
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!isPlaying()) return;

        // Map
        g.drawImage(currentMap.image, 0, 0, null);

        // Engine
        coreEngine.renderDraw((Graphics2D) g);

        // Debug
        if (DEBUG) drawDebug(g);
    }

    @Override
    public void update() {
        pacman.update();

        for (Player ghost : ghosts) {
            ghost.update();
        }
    }
}
