package core.engine;


import physics.engine.IPhysicsEngine;
import physics.game.IPhyObject;
import rendering.engine.IRenderEngine;
import rendering.game.IEntity;

import java.util.Vector;

public class CoreEngine implements ICoreEngine {

    private IRenderEngine renderEngine;
    private IPhysicsEngine physicsEngine;

    private Vector<IEntity> graphics = new Vector<IEntity>();
    private Vector<IPhyObject> characters = new Vector<IPhyObject>();

    public CoreEngine(IRenderEngine renderEngine, IPhysicsEngine physicsEngine) {
        this.renderEngine = renderEngine;
        this.physicsEngine = physicsEngine;
    }

    public void addCharacter(int x, int width, int y, int height, int velocityX, int velocityY, String spritePath, int spriteWidth, int spriteCount, int loopDelay) {
        IEntity entity = renderEngine.addEntity(spritePath, spriteWidth, spriteCount);
        entity.getSprite().loop(loopDelay);
        IPhyObject object = physicsEngine.addObject(x, width, y, height);
        object.setVelocity(velocityX, velocityY);

        graphics.add(entity);
        characters.add(object);
    }

    public void run() throws InterruptedException {
        for (; ; ) {
            ((IEngine) physicsEngine).update();

            for (int i = 0; i < graphics.size(); i++) {
                IEntity entity = graphics.get(i);
                IPhyObject character = characters.get(i);

                entity.setPosition(character.getX(), character.getY());
            }

            ((IEngine) renderEngine).update();
            Thread.sleep(20);
        }
    }

}
