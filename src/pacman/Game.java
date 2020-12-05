package pacman;

import framework.AI.IAIModel;
import framework.IGameEngine;
import framework.core.CoreEngine;
import framework.geometry.Point;
import framework.geometry.Rect;
import framework.rendering.IPanel;
import pacman.AI.*;
import pacman.utility.FontsEngine;
import pacman.windows.MainWindow;
import pacman.windows.SplashWindow;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Vector;

public class Game extends JPanel implements IPanel, IGameEngine {
    // --- Statics
    public static final int STEP_SIZE = 8;
    public static final int PLAYER_SIZE = 32;
    public static final int TILE_SIZE = STEP_SIZE * 2;
    final static int SPRITE_WIDTH = 28;
    final static int PAC_SPRITE_COUNT = 10;
    final static int PAC_DEATH_SPRITE_COUNT = 17;
    final static int GHOST_SPRITE_COUNT = 4;
    final static int GHOST_PRISON_EXIT_DELAY_MS = 500;
    public static Game current;
    static SplashWindow splashWindow;
    static MainWindow mainWindow;
    static boolean DEBUG = false;
    // ---


    private Pacman pacman;
    private Vector<Ghost> ghosts;
    private CoreEngine coreEngine;
    public Map map;
    public Level level;
    private GhostMode mode;

    private HashMap<Ghost, Long> ghostReleaseTime = new HashMap<>();
    private long changeModeAt = 0;

    public Game() {
        coreEngine = new CoreEngine(this, this);
        setBackground(Color.black);
        mode = GhostMode.CHASE;
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
        current = new Game();

        current.play("test");
    }


    public void load(String levelIdentifier) {
        // Load assets
        try {
            // Level
            level = MemoryDB.getLevel(levelIdentifier);

            // Map
            map = MemoryDB.getMap(level.mapIdentifier);

            // Pacman
            pacman = Pacman.createPacman(level.pacman.skinId, level.pacman.speed, map.pacmanSpawn);
            coreEngine.addCharacter(pacman.getCharacter());

            // Ghosts
            ghosts = new Vector<>();
            for (Level.Actor p : level.ghosts) {
                Ghost ghost = Ghost.createGhost(p.skinId, p.typeId, p.speed, map.ghostPrison);
                coreEngine.addCharacter(ghost.getCharacter());
                ghosts.add(ghost);
            }

            // Set preset arrows to pacman
            coreEngine.addInputSource(
                    pacman.bindControls(
                            MemoryDB.loadPreset("arrows")
                    )
            );

         // Set AI's based on typeId
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
                coreEngine.addAIController(ghost.getController());
            }

            // Start
            restart();

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void setInPrison(Ghost ghost, int extraDelay) {
        ghost.changeMode(GhostMode.PRISONED);
        ghostReleaseTime.put(ghost, System.currentTimeMillis() + GHOST_PRISON_EXIT_DELAY_MS + extraDelay);
    }

    private void setExitPrison(Ghost ghost) {
        ghost.changeMode(GhostMode.EXIT_PRISON);
    }

    private boolean isPlaying() {
        return level != null;
    }

    public void play(String levelIdentifier) {
        // Load game
        mainWindow.setVisible(false);
        SwingUtilities.invokeLater(() -> splashWindow.setVisible(true));
        load(levelIdentifier);

        // Hook into window
        mainWindow.setPanel(this, map.width, map.height);
        SwingUtilities.invokeLater(() -> {
            splashWindow.setVisible(false);
            mainWindow.setVisible(true);
        });

        // run engine
        Thread coreThread = new Thread(coreEngine);
        coreThread.start();
        changeModeAt = System.currentTimeMillis() + level.chaseDuration;
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

    private void debugAI(Graphics g, Ghost ghost) {
        IAIModel ai = ghost.getModel();
        if (ai instanceof BlinkyAI) g.setColor(Color.red);
        else if (ai instanceof PinkyAI) g.setColor(Color.pink);
        else if (ai instanceof InkyAI) g.setColor(Color.cyan);
        else if (ai instanceof ClydeAI) g.setColor(Color.orange);

        Point target = ghost.getTarget();
        g.fillRect(target.getX(), target.getY(), Game.STEP_SIZE, Game.STEP_SIZE);
        g.drawString(ai.getClass().getSimpleName(), target.getX(), target.getY() - 2);
    }


    private void drawDebug(Graphics g) {
        // Walls hitbox
        g.setColor(Color.red);
        for (Rect wall : map.walls) {
            g.drawRect(wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight());
        }

        // Prison wall
        g.setColor(Color.white);
        g.drawRect(map.prisonWall.getX(), map.prisonWall.getY(), map.prisonWall.getWidth(), map.prisonWall.getHeight());

        // Game hitboxes
        debugPlayer(g, Color.green, pacman);

        // Ghosts hitboxes & IA
        for (Ghost ghost : ghosts) {
            debugPlayer(g, Color.yellow, ghost);
            if (!ghost.playerControlled())
                debugAI(g, ghost);
        }
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!isPlaying()) return;

        // Map
        g.drawImage(map.image, 0, 0, null);

        // Engine
        coreEngine.renderDraw((Graphics2D) g);

        // Debug
        if (DEBUG) drawDebug(g);
    }

    private boolean restarting = false;

    public void restart() {
        mode = GhostMode.CHASE;
        changeModeAt = System.currentTimeMillis() + level.chaseDuration;
        pacman.setPosition(map.pacmanSpawn);
        pacman.resurrect();
        int i = 0;
        for (Ghost ghost: ghosts) {
            ghost.setPosition(map.ghostPrison);
            setInPrison(ghost, i*GHOST_PRISON_EXIT_DELAY_MS);
            i++;
        }
        restarting = false;
    }

    public void caught() {
        restarting = true;
        pacman.kill(() -> restart());
        for (Ghost ghost: ghosts) {
            ghost.setDisabled(true);
        }
    }

    @Override
    public void update() {
        if(restarting) return;

        long currentMs = System.currentTimeMillis();

        pacman.update();

        boolean changeMode = false;
        if (currentMs > changeModeAt) {
            changeMode = true;
            mode = mode == GhostMode.CHASE ? GhostMode.SCATTER : GhostMode.CHASE;
            changeModeAt = currentMs + (mode == GhostMode.CHASE ? level.chaseDuration : level.scatterDuration);
        }

        for (Ghost ghost : ghosts) {
            ghost.update();

            // Hitting pacman
            if (ghost.getHitbox().intersect(pacman.getHitbox())) {
                caught();
                return;
            }

            // Exit prison
            if (ghost.inPrison() && currentMs > ghostReleaseTime.get(ghost)) {
                setExitPrison(ghost);
            }
            // Exited prison
            if (ghost.exitedPrison()) {
                ghost.changeMode(mode);
            }
            // Change mode
           if (ghost.controllable() && changeMode) {
                ghost.changeMode(mode);
            }
        }
    }
}
