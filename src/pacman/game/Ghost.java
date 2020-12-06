package pacman.game;

import framework.AI.IAIModel;
import framework.core.Character;
import framework.core.CoreEngine;
import framework.geometry.Point;
import framework.physics.PhysicsEngine;
import framework.rendering.GraphicObject;
import framework.rendering.RenderEngine;
import framework.rendering.graphics.SpriteSheet;
import pacman.AI.*;
import pacman.Game;
import pacman.parsing.MemoryDB;

import java.io.IOException;
import java.util.HashMap;

public class Ghost extends Player {
    public HashMap<PlayerDirection, SpriteSheet> weakSheets;
    public HashMap<PlayerDirection, SpriteSheet> dangerSheets;
    int originalSpeed;
    HashMap<PlayerDirection, SpriteSheet> currentSheets;
    private String controllerId;
    private IAIModel model = null;
    private GhostController controller;
    private GhostMode mode;

    public Ghost(Character character, int speed) {
        super(character, speed);
        originalSpeed = speed;
        currentSheets = directionsSheet;
    }

    public static Ghost createGhost(String skinId, String controllerId, int speed, Point position) throws IOException {
        SpriteSheet left = MemoryDB.getSpriteSheet(skinId + "/left", Game.SPRITE_WIDTH);
        SpriteSheet right = MemoryDB.getSpriteSheet(skinId + "/right", Game.SPRITE_WIDTH);
        SpriteSheet up = MemoryDB.getSpriteSheet(skinId + "/up", Game.SPRITE_WIDTH);
        SpriteSheet down = MemoryDB.getSpriteSheet(skinId + "/down", Game.SPRITE_WIDTH);


        SpriteSheet weak_left = MemoryDB.getSpriteSheet("weak/left", Game.SPRITE_WIDTH);
        SpriteSheet weak_right = MemoryDB.getSpriteSheet("weak/right", Game.SPRITE_WIDTH);
        SpriteSheet weak_up = MemoryDB.getSpriteSheet("weak/up", Game.SPRITE_WIDTH);
        SpriteSheet weak_down = MemoryDB.getSpriteSheet("weak/down", Game.SPRITE_WIDTH);

        SpriteSheet danger_left = MemoryDB.getSpriteSheet("dangerous/left", Game.SPRITE_WIDTH);
        SpriteSheet danger_right = MemoryDB.getSpriteSheet("dangerous/right", Game.SPRITE_WIDTH);
        SpriteSheet danger_up = MemoryDB.getSpriteSheet("dangerous/up", Game.SPRITE_WIDTH);
        SpriteSheet danger_down = MemoryDB.getSpriteSheet("dangerous/down", Game.SPRITE_WIDTH);

        GraphicObject pGraph = RenderEngine.createObject(up);
        pGraph.getSprite().loop(200 / up.getSpriteCount());

        Character character = CoreEngine.createCharacter(pGraph, PhysicsEngine.createObject(position.getX(), Game.SPRITE_WIDTH, position.getY(), Game.SPRITE_WIDTH));
        Ghost ghost = new Ghost(character, speed);
        ghost.setControllerId(controllerId);

        ghost.weakSheets = new HashMap<>();
        ghost.weakSheets.put(PlayerDirection.UP, weak_up);
        ghost.weakSheets.put(PlayerDirection.DOWN, weak_down);
        ghost.weakSheets.put(PlayerDirection.LEFT, weak_left);
        ghost.weakSheets.put(PlayerDirection.RIGHT, weak_right);

        ghost.dangerSheets = new HashMap<>();
        ghost.dangerSheets.put(PlayerDirection.UP, danger_up);
        ghost.dangerSheets.put(PlayerDirection.DOWN, danger_down);
        ghost.dangerSheets.put(PlayerDirection.LEFT, danger_left);
        ghost.dangerSheets.put(PlayerDirection.RIGHT, danger_right);

        ghost.bindDirection(PlayerDirection.UP, up);
        ghost.bindDirection(PlayerDirection.DOWN, down);
        ghost.bindDirection(PlayerDirection.LEFT, left);
        ghost.bindDirection(PlayerDirection.RIGHT, right);

        return ghost;
    }

    public boolean playerControlled() {
        return model == null;
    }

    public GhostController getController() {
        return controller;
    }

    public boolean inPrison() {
        return mode == GhostMode.PRISONED || mode == GhostMode.EXIT_PRISON;
    }

    public boolean controllable() {
        return mode == GhostMode.CHASE || mode == GhostMode.SCATTER;
    }

    public IAIModel getModel() {
        return model;
    }

    public void setModel(IAIModel model) {
        this.model = model;
        controller = new GhostController(this, model);
    }

    public String getControllerId() {
        return controllerId;
    }

    public void setControllerId(String controllerId) {
        this.controllerId = controllerId;
    }

    public Point getTarget() {
        return controller.getTarget();
    }

    @Override
    public void changeDirection(PlayerDirection direction) {
        super.changeDirection(direction);

        applySprite();
    }

    private void applySprite() {
        getCharacter().getGraphicObject().getSprite().setSpriteSheet(
                currentSheets.get(getDirection())
        );
    }

    private void setFrightenedSprite() {
        currentSheets = weakSheets;
        applySprite();
    }

    public void setDangerSprite() {
        currentSheets = dangerSheets;
        applySprite();
    }

    private void setNormalSprite() {
        currentSheets = directionsSheet;

        applySprite();
    }

    private void endFrighten() {
        setNormalSprite();
        setSpeed(originalSpeed);
    }

    public void changeMode(GhostMode mode) {
        if (mode == this.mode) return;
        setDisabled(false);
        switch (mode) {
            case PRISONED:
                setDisabled(true);
                endFrighten();
                getSprite().setScale(1f);
                changeDirection(PlayerDirection.UP);
                break;
            case EXIT_PRISON:
                controller.setForcedTarget(Game.current.map.ghostSpawn);
                break;
            case ENTER_PRISON:
                controller.setForcedTarget(Game.current.map.ghostPrison);
                break;
            case CHASE:
                endFrighten();
                controller.clearForcedTarget();
                break;
            case SCATTER:
                endFrighten();
                Point target = null;
                if (model instanceof BlinkyAI) target = BlinkyAI.scatterPosition();
                else if (model instanceof ClydeAI) target = ClydeAI.scatterPosition();
                else if (model instanceof InkyAI) target = InkyAI.scatterPosition();
                else if (model instanceof PinkyAI) target = PinkyAI.scatterPosition();
                controller.setForcedTarget(target);
                break;
            case FRIGHTENED:
                setFrightenedSprite();
                controller.clearForcedTarget();
                setSpeed(originalSpeed / 2);
                break;
            case DEAD:
                endFrighten();
                controller.setForcedTarget(Game.current.map.ghostSpawn);
                setSpeed(Game.DEAD_GHOST_SPEED);
                getSprite().setScale(0.5f);
                break;
        }

        this.mode = mode;
    }

    public boolean isDead() {
        return mode == GhostMode.DEAD || mode == GhostMode.ENTER_PRISON;
    }

    public boolean onPrisonEntry() {
        return Game.current.map.ghostSpawn.equals(getPosition());
    }

    public boolean onPrisonInside() {
        return Game.current.map.ghostPrison.equals(getPosition());
    }

    public boolean isFrightned() {
        return mode == GhostMode.FRIGHTENED;
    }


    @Override
    public boolean willHitWall(PlayerDirection direction) {
        if (mode != GhostMode.EXIT_PRISON && mode != GhostMode.ENTER_PRISON && mode != GhostMode.DEAD && Game.current.map.prisonWall.intersect(nextHitbox(direction))) {
            return true;
        }
        return super.willHitWall(direction);
    }

}
