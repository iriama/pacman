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

public class Pacman extends JPanel implements IPanel, IGameEngine {
    // --- Statics
    public static final int STEP_SIZE = 8;
    public static final int PLAYER_SIZE = 32;
    public static final int TILE_SIZE = STEP_SIZE * 2;
    final static int SPRITE_WIDTH = 28;
    final static int PAC_SPRITE_COUNT = 10;
    final static int PAC_DEATH_SPRITE_COUNT = 17;
    final static int GHOST_SPRITE_COUNT = 4;
    public static Pacman game;
    static SplashWindow splashWindow;
    static MainWindow mainWindow;
    static boolean DEBUG = false;
    // ---
    private HashMap<String, Map> maps;
    private HashMap<String, SpriteSheet> spriteSheets;
    private HashMap<String, Level> levels;
    private HashMap<String, Preset> presets;
    private HashMap<String, IAIModel> models;
    private Player pacman;
    private Vector<Player> ghosts;
    private CoreEngine coreEngine;
    public Map currentMap;
    public Level currentLevel;

    public Pacman() {
        maps = new HashMap<>();
        levels = new HashMap<>();
        spriteSheets = new HashMap<>();
        presets = new HashMap<>();
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
        game = new Pacman();

        game.play("test");
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


    private Player loadPlayer(Level.Actor actor, int spriteWidth, int spriteCount) throws IOException {
        SpriteSheet left = getSpriteSheet(actor.skinId + "/left", spriteWidth, spriteCount);
        SpriteSheet right = getSpriteSheet(actor.skinId + "/right", spriteWidth, spriteCount);
        SpriteSheet up = getSpriteSheet(actor.skinId + "/up", spriteWidth, spriteCount);
        SpriteSheet down = getSpriteSheet(actor.skinId + "/down", spriteWidth, spriteCount);

        GraphicObject pGraph = RenderEngine.createObject(right);
        pGraph.getSprite().loop(200 / spriteCount);

        Player player = new Player(
                actor.typeId,
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

            // Pacman
            pacman = loadPlayer(currentLevel.pacman, SPRITE_WIDTH, PAC_SPRITE_COUNT);


            ghosts = new Vector<>();
            for (Level.Actor p : currentLevel.ghosts) {
                ghosts.add(loadPlayer(p, SPRITE_WIDTH, GHOST_SPRITE_COUNT));
            }

            // Set preset arrows to pacman
            coreEngine.addInputSource(
                    pacman.bindControls(
                            loadPreset("arrows")
                    )
            );

            // Set AI's based on typeId
            models = new HashMap<>(); // for debug drawings
            for (Player ghost : ghosts) {
                IAIModel model = null;

                if (ghost.getTypeId().equals("clyde")) {
                    model = new ClydeAI(pacman, ghost);
                } else if (ghost.getTypeId().equals("pinky")) {
                    model = new PinkyAI(pacman);
                } else if (ghost.getTypeId().equals("inky")) {
                    innerLoop:
                    for (Player g : ghosts) {
                        if (g.getTypeId().equals("blinky")) {
                            model = new InkyAI(pacman, g);
                            break innerLoop;
                        }
                    }
                }

                if (model == null) model = new BlinkyAI(pacman); // defaults to blinky

                models.put(ghost.getTypeId(), model);
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
        g.fillRect(prediction.getX(), prediction.getY(), Pacman.STEP_SIZE, Pacman.STEP_SIZE);
        g.drawString(ai.getClass().getSimpleName(), prediction.getX(), prediction.getY() - 2);
    }


    private void drawDebug(Graphics g) {
        // Walls hitbox
        g.setColor(Color.red);
        for (Rect wall : currentMap.walls) {
            g.drawRect(wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight());
        }

        // Pacman hitboxes
        debugPlayer(g, Color.green, pacman);

        // Ghosts hitboxes
        for (Player ghost : ghosts) {
            debugPlayer(g, Color.yellow, ghost);
        }

        // AI targets
        for (IAIModel ai : models.values()) {
            debugAI(g, ai);
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
