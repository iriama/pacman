package core;

import physics.game.Character;
import physics.game.Direction;
import physics.game.PhysicsEngine;
import physics.game.Size;
import rendering.engine.IRenderEngine;
import rendering.engine.RenderEngine;
import rendering.game.Entity;

import java.util.Vector;

public class CoreEngine {

    private RenderEngine renderEngine;
    private PhysicsEngine physicsEngine;

    private Vector<Entity> entities = new Vector<Entity>();
    private Vector<Character> characters = new Vector<Character>();

    public CoreEngine(RenderEngine renderEngine, PhysicsEngine physicsEngine) {
        this.renderEngine = renderEngine;
        this.physicsEngine = physicsEngine;
    }

    public void addCharacter(int x, int y, int height, int width, int speed, Direction direction, String spritePath, int spriteCount, int delay) {
        Entity entity = renderEngine.addEntity(spritePath, width, spriteCount);
        entity.getSprite().loop(delay);
        Character character = new Character(x, y, new Size(height, width), speed, direction);
        physicsEngine.addCharacter(character);

        entities.add(entity);
        characters.add(character);
    }

    public void run() throws InterruptedException {
        for (;;) {
            physicsEngine.update();

            for (int i=0; i<entities.size(); i++) {
                Entity entity = entities.get(i);
                Character character = characters.get(i);

                entity.setPosition(character.getX(), character.getY());
            }

            renderEngine.update();
            Thread.sleep(10);
        }
    }

}
