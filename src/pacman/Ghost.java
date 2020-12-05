package pacman;

import framework.AI.IAIModel;
import framework.core.Character;
import framework.core.CoreEngine;
import framework.geometry.Point;
import framework.physics.PhysicsEngine;
import framework.rendering.GraphicObject;
import framework.rendering.RenderEngine;
import framework.rendering.graphics.SpriteSheet;
import pacman.AI.GhostController;

import java.io.IOException;

enum GhostMode {
    PRISONED,
    EXIT_PRISON,
    CHASE,
    SCATTER,
    FRIGHTENED,
    DEAD
}

public class Ghost extends Player {
    private String controllerId;
    private IAIModel model = null;
    private GhostController controller;
    private long lastModeChangeMs;

    private GhostMode mode;

    public Ghost(Character character, int speed) {
        super(character, speed);
    }

    public boolean playerControlled() {
        return model != null;
    }

    public void setModel(IAIModel model) {
        this.model = model;
        controller = new GhostController(this, model);
    }

    public GhostController getController() {
        return controller;
    }

    public boolean inPrison() {
        return mode == GhostMode.PRISONED;
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


    public static Ghost createGhost(String skinId, String controllerId, int speed, Point position, int spriteWidth, int spriteCount) throws IOException {
        SpriteSheet left = MemoryDB.getSpriteSheet(skinId + "/left", spriteWidth, spriteCount);
        SpriteSheet right = MemoryDB.getSpriteSheet(skinId + "/right", spriteWidth, spriteCount);
        SpriteSheet up = MemoryDB.getSpriteSheet(skinId + "/up", spriteWidth, spriteCount);
        SpriteSheet down = MemoryDB.getSpriteSheet(skinId + "/down", spriteWidth, spriteCount);

        GraphicObject pGraph = RenderEngine.createObject(up);
        pGraph.getSprite().loop(200 / spriteCount);

        Character character = CoreEngine.createCharacter(pGraph, PhysicsEngine.createObject(position.getX(), spriteWidth, position.getY(), spriteWidth));
        Ghost ghost = new Ghost(character, speed);
        ghost.setControllerId(controllerId);

        ghost.bindDirection(PlayerDirection.UP, up);
        ghost.bindDirection(PlayerDirection.DOWN, down);
        ghost.bindDirection(PlayerDirection.LEFT, left);
        ghost.bindDirection(PlayerDirection.RIGHT, right);

        return ghost;
    }

    public long getLastModeChangeMs() {
        return lastModeChangeMs;
    }

    public void changeMode(GhostMode mode) {
        lastModeChangeMs = System.currentTimeMillis();
        this.mode = mode;
        switch (mode) {
            case PRISONED:
                setDisabled(true);
                stop();
                break;
            case EXIT_PRISON:
                controller.setForcedTarget(Game.current.map.ghostSpawn);
                setDisabled(false);
                resume();
                break;
            case CHASE:
                controller.clearForcedTarget();
                setDisabled(false);
                resume();
                break;
            case SCATTER:
                break;
            case FRIGHTENED:
                break;
            case DEAD:
                break;
        }
    }

    @Override
    public boolean willHitWall(PlayerDirection direction) {
        if (mode != GhostMode.EXIT_PRISON && mode != GhostMode.DEAD && Game.current.map.prisonWall.intersect(nextHitbox(direction))) {
            return true;
        }
        return super.willHitWall(direction);
    }

    @Override
    public void update() {

        // Mode calculation
        if (mode == GhostMode.EXIT_PRISON && Game.current.map.ghostSpawn.equals(getPosition())) {
            changeMode(GhostMode.CHASE);
        }

        super.update();
    }
}
