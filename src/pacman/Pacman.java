package pacman;

import framework.core.Character;
import framework.core.CoreEngine;
import framework.geometry.Point;
import framework.physics.PhysicsEngine;
import framework.rendering.GraphicObject;
import framework.rendering.RenderEngine;
import framework.rendering.graphics.ISpriteEvent;
import framework.rendering.graphics.Sprite;
import framework.rendering.graphics.SpriteSheet;

import java.io.IOException;

public class Pacman extends Player{

    SpriteSheet deathSheet;
    SpriteSheet defaultSheet;

    public Pacman(Character character, int speed, SpriteSheet defaultSheet, SpriteSheet deathSheet) {
        super(character, speed);
        this.deathSheet = deathSheet;
        this.defaultSheet = defaultSheet;
    }

    public void resurrect() {
        setDisabled(false);
        Sprite sprite = getSprite();
        sprite.setSpriteSheet(defaultSheet);
        sprite.setFrame(2);
        sprite.loop(200 / defaultSheet.getSpriteCount());
        changeDirection(PlayerDirection.UP);
    }

    public void kill(ISpriteEvent onfinish) {
        setDisabled(true);
        Sprite sprite = getSprite();
        sprite.setSpriteSheet(deathSheet);
        sprite.setFrame(0);
        sprite.setLoop(false);
        sprite.play(50);
        sprite.onPlayFinish(onfinish);
    }

    public static Pacman createPacman(String skinId, int speed, Point position) throws IOException {
        SpriteSheet left = MemoryDB.getSpriteSheet(skinId + "/left", Game.SPRITE_WIDTH);
        SpriteSheet right = MemoryDB.getSpriteSheet(skinId + "/right", Game.SPRITE_WIDTH);
        SpriteSheet up = MemoryDB.getSpriteSheet(skinId + "/up", Game.SPRITE_WIDTH);
        SpriteSheet down = MemoryDB.getSpriteSheet(skinId + "/down", Game.SPRITE_WIDTH);
        SpriteSheet deathSheet = MemoryDB.getSpriteSheet(skinId + "/dead", Game.SPRITE_WIDTH);


        GraphicObject pGraph = RenderEngine.createObject(up);
        pGraph.getSprite().loop(200 / up.getSpriteCount());

        Character character = CoreEngine.createCharacter(pGraph, PhysicsEngine.createObject(position.getX(), Game.SPRITE_WIDTH, position.getY(), Game.SPRITE_WIDTH));
        Pacman pacman = new Pacman(character, speed, up, deathSheet);

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
