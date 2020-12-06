package pacman;

import framework.AI.IAIModel;
import framework.IGameEngine;
import framework.core.Character;
import framework.core.CoreEngine;
import framework.geometry.Point;
import framework.geometry.Rect;
import framework.physics.PhysicsEngine;
import framework.rendering.IPanel;
import framework.rendering.RenderEngine;
import pacman.AI.BlinkyAI;
import pacman.AI.ClydeAI;
import pacman.AI.InkyAI;
import pacman.AI.PinkyAI;
import pacman.game.*;
import pacman.parsing.Actor;
import pacman.parsing.Level;
import pacman.parsing.Map;
import pacman.parsing.MemoryDB;
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
    public static final int JETON_SIZE = STEP_SIZE * 2;
    public static final int PLAYER_SIZE = STEP_SIZE * 4;
    public static final int TILE_SIZE = STEP_SIZE * 2;
    public final static int SPRITE_WIDTH = 28;
    public final static int DEAD_GHOST_SPEED = 4;
    public final static int GHOST_PRISON_EXIT_DELAY_MS = 500;
    private final static int JETON_SCORE_VALUE = 1;
    private final static int GHOST_EAT_SCORE_VALUE = 5;
    public static Game current;
    static MainWindow mainWindow;
    static boolean DEBUG = false;
    // ---
    public Map map;
    public Level level;
    private Pacman pacman;
    private Vector<Ghost> ghosts;
    private Vector<Character> jetons;
    private Vector<Character> specialJetons;
    private CoreEngine coreEngine;
    private GhostMode mode;
    private int score = 0;
    private int time = 0;
    private int lives = 1;
    private int saveScore = 0;

    private HashMap<Ghost, Long> ghostReleaseTime = new HashMap<>();
    private HashMap<Ghost, Long> ghostFrightnedEndTime = new HashMap<>();
    private long changeModeAt = 0;
    private boolean restarting = false;
    private boolean finished = false;

    private StatusBar statusBar;
    private IGameEvent onSuccess;
    private IGameEvent onFail;
    private String levelIdentifier;
    private String pacmanPreset;


    public void SoloGame(int saveScore, String levelIdentifier, String preset, StatusBar statusBar, IGameEvent onSuccess, IGameEvent onFail) {
        current = this;
        build();
        this.statusBar = statusBar;
        this.onSuccess = onSuccess;
        this.onFail = onFail;
        coreEngine = new CoreEngine(this, this);
        this.levelIdentifier = levelIdentifier;
        this.saveScore = saveScore;
        this.pacmanPreset = preset;
        play();
    }

    private void build() {
        setBackground(Color.black);
        setLayout(null);
    }

    private void load() {
        // Load assets
        try {
            // ---- Level
            level = MemoryDB.getLevel(levelIdentifier);

            // ---- Map
            map = MemoryDB.getMap(level.mapIdentifier);


            // ---- Pacman
            pacman = Pacman.createPacman(level.pacman.skinId, level.pacman.speed, map.pacmanSpawn);
            coreEngine.addCharacter(pacman.getCharacter());

            // ---- JETONS
            // Add special jetons
            this.specialJetons = new Vector<>();
            for (Rect special : map.specialJetons) {
                this.specialJetons.add(
                        coreEngine.addCharacter(
                                RenderEngine.createObject(
                                        MemoryDB.getSpriteSheet("game/special", JETON_SIZE)
                                ),
                                PhysicsEngine.createObject(special.getX(), special.getWidth(), special.getY(), special.getHeight())
                        )
                );
            }

            // Recursively generate basic jetons
            Vector<Rect> temp = new Vector<>();
            generateJetons(temp, pacman.getPosition().extend(new Point(STEP_SIZE, STEP_SIZE)));
            // Add basic jetons to core engine
            this.jetons = new Vector<>();
            for (Rect jeton : temp) {
                this.jetons.add(
                        coreEngine.addCharacter(
                                RenderEngine.createObject(
                                        MemoryDB.getSpriteSheet("game/jeton", JETON_SIZE)
                                ),
                                PhysicsEngine.createObject(jeton.getX(), jeton.getWidth(), jeton.getY(), jeton.getHeight()))
                );
            }


            // ---- Ghosts
            ghosts = new Vector<>();
            for (Actor p : level.ghosts) {
                Ghost ghost = Ghost.createGhost(p.skinId, p.typeId, p.speed, map.ghostPrison);
                coreEngine.addCharacter(ghost.getCharacter());
                ghosts.add(ghost);
            }

            // ---- INPUT
            coreEngine.addInputSource(
                    pacman.bindControls(
                            MemoryDB.loadPreset(pacmanPreset)
                    )
            );

            // ---- IA
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


            // ---- COUNTERS
            resetScore();
            resetTime();
            resetLives();

            // ---- PLAY
            replay();

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void resetTime() {
        lastSecondTick = null;
        time = 0;
        statusBar.setTime(time);
    }

    private void incrementTime(int value) {
        time += value;
        statusBar.setTime(time);
    }

    private void resetLives() {
        lives = level.lives;
        statusBar.setLives(lives);
    }

    private void decrementLives() {
        lives--;
        statusBar.setLives(lives);
    }

    private void resetScore() {
        score = saveScore;
        statusBar.setScore(score);
    }

    private void incrementScore(int value) {
        score += value;
        statusBar.setScore(score);
    }

    private void generateJetons(Vector<Rect> jetons, Point current) {
        outerLoop:
        for (PlayerDirection direction : PlayerDirection.values()) {
            int modX = direction == PlayerDirection.LEFT ? -JETON_SIZE : direction == PlayerDirection.RIGHT ? JETON_SIZE : 0;
            int modY = direction == PlayerDirection.UP ? -JETON_SIZE : direction == PlayerDirection.DOWN ? JETON_SIZE : 0;

            Rect jeton = new Rect(
                    current.getX() + modX,
                    JETON_SIZE,
                    current.getY() + modY,
                    JETON_SIZE
            );

            if (jetons.contains(jeton) || jeton.getX() < 0 || jeton.getX() >= map.width || jeton.getY() < 0 || jeton.getY() >= map.height)
                continue;

            if (jeton.intersect(map.prisonWall)) continue;
            for (Rect wall : map.walls) {
                if (jeton.intersect(wall)) continue outerLoop;
            }

            jetons.add(jeton);
            generateJetons(jetons, jeton.getPosition());

            // Cleanup jetons on pacman
            if (jeton.intersect(pacman.getWallHitbox()))
                jetons.remove(jeton);
            // Cleanup jetons on special jetons
            if (map.specialJetons.contains(jeton))
                jetons.remove(jeton);
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

    public void play() {
        // -- LOAD GAME
        load();

        // Setup other components
        statusBar.setBounds(map.width, 0, StatusBar.WIDTH, map.height);
        add(statusBar);

        statusBar.setLevel(levelIdentifier);

        // Hook into window
        mainWindow.setPanel(this, map.width + StatusBar.WIDTH, map.height);

        // run engine
        Thread coreThread = new Thread(coreEngine);
        coreThread.start();

        // after engine running
        changeModeAt = System.currentTimeMillis() + level.chaseDuration;
    }

    private void debugPlayer(Graphics g, Color color, Player player) {
        g.setColor(color);
        Rect hitbox = player.getEffectiveHitbox();
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

        // Jetons
        for (int i = 0; i < jetons.size(); i++) {
            g.setColor(Color.CYAN);
            Rect hitbox = jetons.get(i).getPhyObject().getHitbox();
            g.drawRect(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
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


    public void replay() {
        if (lives < 1) {
            onFail.action(score, lives, time);
            return;
        }

        mode = GhostMode.CHASE;
        changeModeAt = System.currentTimeMillis() + level.chaseDuration;
        pacman.setPosition(map.pacmanSpawn);
        pacman.resurrect();
        int i = 0;
        for (Ghost ghost : ghosts) {
            ghost.setPosition(map.ghostPrison);
            setInPrison(ghost, i * GHOST_PRISON_EXIT_DELAY_MS);
            i++;
        }
        restarting = false;
    }

    public void caught() {
        restarting = true;
        pacman.kill(() -> replay());
        for (Ghost ghost : ghosts) {
            ghost.setDisabled(true);
        }
        decrementLives();
    }

    public void ghostCaught(Ghost ghost) {
        ghost.changeMode(GhostMode.DEAD);
        incrementScore(GHOST_EAT_SCORE_VALUE);
    }

    private void checkCleard() {
        if (jetons.size() == 0 && specialJetons.size() == 0) {
            finished = true;
            onSuccess.action(score, lives, time);
        }
    }

    private void eat(Character jeton) {
        jetons.remove(jeton);
        coreEngine.removeCharacter(jeton);
        incrementScore(JETON_SCORE_VALUE);
        checkCleard();
    }

    private void eatSpecial(Character special) {
        specialJetons.remove(special);
        coreEngine.removeCharacter(special);

        for (Ghost ghost : ghosts) {
            if (!ghost.controllable()) continue;

            ghostFrightnedEndTime.put(ghost, System.currentTimeMillis() + level.frightnedDuration);
            ghost.changeMode(GhostMode.FRIGHTENED);
        }
        incrementScore(JETON_SCORE_VALUE);
        checkCleard();
    }

    private Long lastSecondTick = null;

    @Override
    public void update() {
        if (restarting || finished) return;

        long currentMs = System.currentTimeMillis();

        if (lastSecondTick == null)
            lastSecondTick = currentMs;

        // Time
        if (currentMs - lastSecondTick > 1000) {
            lastSecondTick = currentMs;
            incrementTime(1);
            lastSecondTick = currentMs;
        }


        pacman.update();

        // Jetons
        Rect pacmanHitbox = pacman.getEffectiveHitbox();
        for (Character jeton : jetons) {
            Rect jbox = jeton.getPhyObject().getHitbox();
            if (jbox.intersect(pacmanHitbox)) {
                eat(jeton);
                break;
            }
        }
        // Jeton Special
        for (Character special : specialJetons) {
            Rect jbox = special.getPhyObject().getHitbox();
            if (jbox.intersect(pacmanHitbox)) {
                eatSpecial(special);
                break;
            }
        }

        boolean changeMode = false;
        if (currentMs > changeModeAt) {
            changeMode = true;
            mode = mode == GhostMode.CHASE ? GhostMode.SCATTER : GhostMode.CHASE;
            changeModeAt = currentMs + (mode == GhostMode.CHASE ? level.chaseDuration : level.scatterDuration);
        }

        for (Ghost ghost : ghosts) {
            ghost.update();

            // DEAD
            if (ghost.isDead()) {
                if (ghost.onPrisonEntry()) { // On prison entry
                    ghost.changeMode(GhostMode.ENTER_PRISON);
                } else if (ghost.onPrisonInside()) { // Inside prison
                    setInPrison(ghost, level.ghostKillDuration);
                }
                continue;
            }

            // Hitting pacman
            if (ghost.getEffectiveHitbox().intersect(pacmanHitbox)) {
                if (ghost.isFrightned()) {
                    ghostCaught(ghost);
                } else {
                    caught();
                    return;
                }

                continue;
            }

            // Frightned
            if (ghost.isFrightned()) {
                long endTime = ghostFrightnedEndTime.get(ghost);
                if (currentMs > endTime) {
                    ghost.changeMode(mode);
                } else if (currentMs + Math.max(1000, level.frightnedDuration / 5) > endTime) {
                    ghost.setDangerSprite();
                }
                continue;
            }


            // In prison
            if (ghost.inPrison()) {
                if (currentMs > ghostReleaseTime.get(ghost)) { // Released
                    setExitPrison(ghost);
                }
                // Exited prison
                if (ghost.onPrisonEntry()) { // On prison entry
                    ghost.changeMode(mode);
                }

                continue;
            }

            // Change mode
            if (ghost.controllable()) {
                if (changeMode) {
                    ghost.changeMode(mode);
                }
                continue;
            }
        }
    }
}
