package pacman;

import framework.AI.IAIModel;
import framework.core.Character;
import framework.core.CoreEngine;
import framework.geometry.Point;
import framework.physics.PhysicsEngine;
import framework.rendering.GraphicObject;
import framework.rendering.RenderEngine;
import framework.rendering.graphics.SpriteSheet;

import java.io.IOException;

public class Ghost extends Player {
    String controllerId;
    IAIModel model = null;

    public Ghost(Character character, int speed) {
        super(character, speed);
    }
    public boolean playerControlled() {
        return model != null;
    }

    public void setModel(IAIModel model) {
        this.model = model;
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

        GraphicObject pGraph = RenderEngine.createObject(right);
        pGraph.getSprite().loop(200 / spriteCount);

        Character character = CoreEngine.createCharacter(pGraph, PhysicsEngine.createObject(position.getX(), Game.PLAYER_SIZE, position.getY(), Game.PLAYER_SIZE));
        Ghost ghost = new Ghost(character, speed);
        ghost.setControllerId(controllerId);

        ghost.bindDirection(PlayerDirection.UP, up);
        ghost.bindDirection(PlayerDirection.DOWN, down);
        ghost.bindDirection(PlayerDirection.LEFT, left);
        ghost.bindDirection(PlayerDirection.RIGHT, right);

        return ghost;
    }

}
