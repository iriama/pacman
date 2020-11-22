package core.engine;

import core.game.Character;
import core.game.ICharacter;
import core.utility.IdFactory;
import physics.engine.IPhysicsEngine;
import physics.game.IPhyObject;
import physics.game.PhyObject;
import rendering.engine.IRenderEngine;
import rendering.game.GraphicObject;
import rendering.game.IGraphicObject;
import rendering.graphics.Sprite;
import rendering.graphics.SpriteSheet;

import java.util.Vector;

public class CoreEngine implements ICoreEngine {

    private IRenderEngine renderEngine;
    private IPhysicsEngine physicsEngine;
    private Vector<ICharacter> characters;

    public CoreEngine(IRenderEngine renderEngine, IPhysicsEngine physicsEngine) {
        this.renderEngine = renderEngine;
        this.physicsEngine = physicsEngine;
        this.characters = new Vector<ICharacter>();
    }

    public void run() throws InterruptedException {
        for (; ; ) {
            ((IEngine) physicsEngine).update();

            for (ICharacter character : characters) {
                IPhyObject phyObject = character.getPhyObject();
                character.getGraphicObject().setPosition(phyObject.getX(), phyObject.getY());
            }

            ((IEngine) renderEngine).update();
            Thread.sleep(20);
        }
    }

    public ICharacter addCharacter(String spriteSheetPath, int spriteWidth, int spriteCount, int x, int width, int y, int height) {
        IGraphicObject graphicObject = new GraphicObject(
                new Sprite(
                        new SpriteSheet(spriteSheetPath, spriteWidth, spriteCount)
                ), IdFactory.nextId()
        );

        IPhyObject phyObject = new PhyObject(x, width, y, height, IdFactory.nextId());

        return addCharacter(graphicObject, phyObject);
    }

    public ICharacter addCharacter(IGraphicObject graphicObject, IPhyObject phyObject) {
        ICharacter character = new Character(graphicObject, phyObject, IdFactory.nextId());
        characters.add(character);

        renderEngine.addObject(graphicObject);
        physicsEngine.addObject(phyObject);

        return character;
    }

    public void removeCharacter(int characterId) {
        characters.remove(new Character(null, null, characterId));
    }
}
