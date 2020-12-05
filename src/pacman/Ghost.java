package pacman;

import framework.AI.IAIModel;
import framework.core.Character;
import framework.core.CoreEngine;
import framework.geometry.Point;
import framework.physics.PhysicsEngine;
import framework.rendering.GraphicObject;
import framework.rendering.RenderEngine;
import framework.rendering.graphics.SpriteSheet;
import pacman.AI.*;

import java.io.IOException;
import java.util.HashMap;

enum GhostMode {
    PRISONED,
    EXIT_PRISON,
    ENTER_PRISON,
    CHASE,
    SCATTER,
    FRIGHTENED,
    DEAD
}

public class Ghost extends Player {
    private String controllerId;
    private IAIModel model = null;
    private GhostController controller;
    private GhostMode mode;
    int originalSpeed;

    public Ghost(Character character, int speed) {
        super(character, speed);
        originalSpeed = speed;
    }

    public boolean playerControlled() {
        return model == null;
    }

    public void setModel(IAIModel model) {
        this.model = model;
        controller = new GhostController(this, model);
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

    public void setControllerId(String controllerId) {
        this.controllerId = controllerId;
    }

    public String getControllerId() {
        return controllerId;
    }

    public Point getTarget() {
        return controller.getTarget();
    }

    public HashMap<PlayerDirection, SpriteSheet> weakSheets;

    public static Ghost createGhost(String skinId, String controllerId, int speed, Point position) throws IOException {
        SpriteSheet left = MemoryDB.getSpriteSheet(skinId + "/left", Game.SPRITE_WIDTH);
        SpriteSheet right = MemoryDB.getSpriteSheet(skinId + "/right", Game.SPRITE_WIDTH);
        SpriteSheet up = MemoryDB.getSpriteSheet(skinId + "/up", Game.SPRITE_WIDTH);
        SpriteSheet down = MemoryDB.getSpriteSheet(skinId + "/down", Game.SPRITE_WIDTH);


        SpriteSheet weak_left = MemoryDB.getSpriteSheet("weak/left", Game.SPRITE_WIDTH);
        SpriteSheet weak_right = MemoryDB.getSpriteSheet("weak/right", Game.SPRITE_WIDTH);
        SpriteSheet weak_up = MemoryDB.getSpriteSheet("weak/up", Game.SPRITE_WIDTH);
        SpriteSheet weak_down = MemoryDB.getSpriteSheet("weak/down", Game.SPRITE_WIDTH);

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

        ghost.bindDirection(PlayerDirection.UP, up);
        ghost.bindDirection(PlayerDirection.DOWN, down);
        ghost.bindDirection(PlayerDirection.LEFT, left);
        ghost.bindDirection(PlayerDirection.RIGHT, right);

        return ghost;
    }


    @Override
    public void changeDirection(PlayerDirection direction) {
        super.changeDirection(direction);

        if (isFrightned() || isDead()) {
            getCharacter().getGraphicObject().getSprite().setSpriteSheet(
                    weakSheets.get(direction)
            );
        }
    }

    public void changeMode(GhostMode mode) {
        if (mode == this.mode) return;
        switch (mode) {
            case PRISONED:
                setSpeed(originalSpeed);
                getSprite().setScale(1f);
                setDisabled(true);
                changeDirection(PlayerDirection.UP);
                stop();
                break;
            case EXIT_PRISON:
                controller.setForcedTarget(Game.current.map.ghostSpawn);
                setDisabled(false);
                resume();
                break;
            case ENTER_PRISON:
                controller.setForcedTarget(Game.current.map.ghostPrison);
                break;
            case CHASE:
                controller.clearForcedTarget();
                setDisabled(false);
                resume();
                break;
            case SCATTER:
                Point target = null;
                if (model instanceof BlinkyAI) target = BlinkyAI.scatterPosition();
                else if (model instanceof ClydeAI) target = ClydeAI.scatterPosition();
                else if (model instanceof InkyAI) target = InkyAI.scatterPosition();
                else if (model instanceof PinkyAI) target = PinkyAI.scatterPosition();
                controller.setForcedTarget(target);
                setDisabled(false);
                resume();
                break;
            case FRIGHTENED:
                controller.clearForcedTarget();
                setSpeed(originalSpeed / 2);
                break;
            case DEAD:
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

    public boolean inChaseMode() {
        return mode == GhostMode.CHASE;
    }

    public boolean inScatterMode() {
        return mode == GhostMode.SCATTER;
    }

    @Override
    public boolean willHitWall(PlayerDirection direction) {
        if (mode != GhostMode.EXIT_PRISON && mode != GhostMode.ENTER_PRISON && mode != GhostMode.DEAD && Game.current.map.prisonWall.intersect(nextHitbox(direction))) {
            return true;
        }
        return super.willHitWall(direction);
    }

}
