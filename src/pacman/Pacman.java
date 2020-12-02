package pacman;

import framework.IGameEngine;
import framework.core.CoreEngine;
import framework.geometry.Point;
import framework.geometry.Rect;
import framework.physics.PhysicsEngine;
import framework.rendering.GraphicObject;
import framework.rendering.IPanel;
import framework.rendering.RenderEngine;
import framework.rendering.graphics.SpriteSheet;
import pacman.AI.BlinkyAI;
import pacman.AI.GhostController;
import pacman.utility.FontsEngine;
import pacman.windows.MainWindow;
import pacman.windows.SplashWindow;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

public class Pacman extends JPanel implements IPanel, IGameEngine {

    // --- Statics
    static Pacman game;
    static SplashWindow splashWindow;
    static MainWindow mainWindow;
    public static final int TILE_SIZE = 8;
    public static final int PLAYER_SIZE = 32;

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("no map path");
            System.exit(1);
        }

        FontsEngine.start();
        splashWindow = new SplashWindow();
        mainWindow = new MainWindow();
        game = new Pacman();

        game.play("test");
    }

    // ---
    private HashMap<String, Map> maps;
    private HashMap<String, SpriteSheet> spriteSheets;
    private HashMap<String, Level> levels;
    private HashMap<String, Preset> presets;

    private CoreEngine coreEngine;

    public Pacman() {
        maps = new HashMap<>();
        levels = new HashMap<>();
        spriteSheets = new HashMap<>();
        presets = new HashMap<>();
        coreEngine = new CoreEngine(this, this);
        setBackground(Color.black);
    }

    private Map getMap(String identifier) throws Exception {
        if (maps.containsKey(identifier)) return maps.get(identifier);
        Map map = new Map("ressources/maps/" + identifier + ".pacmap");
        maps.put(identifier, map);
        return map;
    }

    private Level getLevel(String identifier) throws Exception {
        if (levels.containsKey(identifier)) return levels.get(identifier);
        Level level = new Level("ressources/levels/" + identifier + ".paclevel");
        levels.put(identifier, level);
        return level;
    }

    private SpriteSheet getSpriteSheet(String identifier, int spriteWidth, int spriteCount) throws IOException {
        if (spriteSheets.containsKey(identifier)) return spriteSheets.get(identifier);
        SpriteSheet spriteSheet = new SpriteSheet("ressources/sprites/" + identifier + ".png", spriteWidth, spriteCount);
        spriteSheets.put(identifier, spriteSheet);
        return spriteSheet;
    }

    private Preset loadPreset(String identifier) throws Exception {
        if (presets.containsKey(identifier)) return presets.get(identifier);
        Preset preset = new Preset("ressources/presets/" + identifier + ".packeys");
        presets.put(identifier, preset);
        return preset;
    }

    Vector<Player> pacmans;
    Vector<Player> ghosts;

    private Player loadPlayer(Actor actor, int spriteWidth, int spriteCount, int loopDelay) throws IOException {
        SpriteSheet left = getSpriteSheet(actor.modelId + "/left", spriteWidth, spriteCount);
        SpriteSheet right = getSpriteSheet(actor.modelId + "/right", spriteWidth, spriteCount);
        SpriteSheet up = getSpriteSheet(actor.modelId + "/up", spriteWidth, spriteCount);
        SpriteSheet down = getSpriteSheet(actor.modelId + "/down", spriteWidth, spriteCount);

        GraphicObject pGraph = RenderEngine.createObject(right);
        pGraph.getSprite().loop(loopDelay);

        Player player = new Player(
                coreEngine.addCharacter(pGraph, PhysicsEngine.createObject(actor.x, actor.width, actor.y, actor.height)),
                actor.speed
        );

        player.bindDirection(PlayerDirection.UP, up);
        player.bindDirection(PlayerDirection.DOWN, down);
        player.bindDirection(PlayerDirection.LEFT, left);
        player.bindDirection(PlayerDirection.RIGHT, right);

        return player;
    }

    public void load(String levelIdentifier) {
        // Load assets
        try {
            // Level
            currentLevel = getLevel(levelIdentifier);

            // Map
            currentMap = getMap(currentLevel.mapIdentifier);

            // Players
            pacmans = new Vector<>();
            for (Actor p: currentLevel.pacmans) {
                pacmans.add(loadPlayer(p, 28, 10, 20));
            }

            ghosts = new Vector<>();
            for (Actor p: currentLevel.ghosts) {
                ghosts.add(loadPlayer(p, 28, 4, 40));
            }

            // Set preset arrows to pac 1
            coreEngine.addInputSource(
                pacmans.get(0).bindControls(
                        loadPreset("arrows")
                )
            );

            // Set preset ZSQD to ghost 1
            coreEngine.addInputSource(
                    ghosts.get(0).bindControls(
                            loadPreset("zsqd")
                    )
            );
            coreEngine.addAIController(new GhostController(ghosts.get(0), new BlinkyAI(ghosts.get(0), pacmans.get(0))));

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public Map currentMap;
    public Level currentLevel;

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

    private void debugCharacter(Graphics g, Color color, Player pacman, String text) {
        g.setColor(color);
        Rect hitbox = pacman.getHitbox();
        g.drawRect(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
        g.setColor(Color.white);
        Rect wallHitbox = pacman.getWallHitbox();
        g.drawRect(wallHitbox.getX(), wallHitbox.getY(), wallHitbox.getWidth(), wallHitbox.getHeight());
        g.setColor(color);
        Point pos = pacman.getPosition();
        g.drawString(text, pos.getX(), pos.getY() - 1);
    }


    private void drawDebug(Graphics g) {
        // Walls hitbox
        g.setColor(Color.red);
        for (Rect wall: currentMap.walls) {
            g.drawRect(wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight());
        }

        // Pacmans hitboxes
        for (Player pacman: pacmans) {
            debugCharacter(g, Color.green, pacman, pacman.getDirection().name());
        }

        // Ghosts hitboxes
        for (Player ghost: ghosts) {
            debugCharacter(g, Color.yellow, ghost, ghost.getDirection().name());
        }
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!isPlaying()) return;

        // Map
        g.drawImage(currentMap.image, 0, 0, null);

        coreEngine.renderDraw((Graphics2D) g);

        // Debug
        drawDebug(g);
    }

    @Override
    public void update() {
        for (Player pacman: pacmans) {
            pacman.update();
        }

        for (Player ghost: ghosts) {
            ghost.update();
        }
    }
}
