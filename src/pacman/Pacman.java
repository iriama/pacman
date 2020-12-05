package pacman;

import framework.core.Character;
import framework.core.CoreEngine;
import framework.geometry.Point;
import framework.physics.PhysicsEngine;
import framework.rendering.GraphicObject;
import framework.rendering.RenderEngine;
import framework.rendering.graphics.SpriteSheet;

import java.io.IOException;

public class Pacman extends Player{

    public Pacman(Character character, int speed) {
        super(character, speed);
    }

    public static Pacman createPacman(String skinId, int speed, Point position, int spriteWidth, int spriteCount) throws IOException {
        SpriteSheet left = MemoryDB.getSpriteSheet(skinId + "/left", spriteWidth, spriteCount);
        SpriteSheet right = MemoryDB.getSpriteSheet(skinId + "/right", spriteWidth, spriteCount);
        SpriteSheet up = MemoryDB.getSpriteSheet(skinId + "/up", spriteWidth, spriteCount);
        SpriteSheet down = MemoryDB.getSpriteSheet(skinId + "/down", spriteWidth, spriteCount);

        GraphicObject pGraph = RenderEngine.createObject(up);
        pGraph.getSprite().loop(200 / spriteCount);

        Character character = CoreEngine.createCharacter(pGraph, PhysicsEngine.createObject(position.getX(), spriteWidth, position.getY(), spriteWidth));
        Pacman pacman = new Pacman(character, speed);

        pacman.bindDirection(PlayerDirection.UP, up);
        pacman.bindDirection(PlayerDirection.DOWN, down);
        pacman.bindDirection(PlayerDirection.LEFT, left);
        pacman.bindDirection(PlayerDirection.RIGHT, right);

        return pacman;
    }

    @Override
    public boolean willHitWall(PlayerDirection direction) {
        return nextHitbox(direction).intersect(Game.current.map.prisonWall) || super.willHitWall(direction);
    }
}
