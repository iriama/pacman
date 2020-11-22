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

public class CoreEngine implements ICoreEngine, IEngine, Runnable {

    private IRenderEngine renderEngine;
    private IPhysicsEngine physicsEngine;
    private Vector<ICharacter> characters;

    final int TICKS_PER_SECOND = 25;
    final int SKIP_TICKS = 1000 / TICKS_PER_SECOND;
    final int MAX_FRAMESKIP = 5;

    public CoreEngine(IRenderEngine renderEngine, IPhysicsEngine physicsEngine) {
        this.renderEngine = renderEngine;
        this.physicsEngine = physicsEngine;
        this.characters = new Vector<ICharacter>();
    }

    /**
     * Update the core engine
     */
    public void update() {
        ((IEngine) physicsEngine).update();

        for (ICharacter character : characters) {
            IPhyObject phyObject = character.getPhyObject();
            character.getGraphicObject().setPosition(phyObject.getX(), phyObject.getY());
        }
    }

    @Override
    public void run() {
        double next_game_tick = System.currentTimeMillis();
        int loops;

        while (true) {
            loops = 0;
            while (System.currentTimeMillis() > next_game_tick && loops < MAX_FRAMESKIP) {

                update();

                next_game_tick += SKIP_TICKS;
                loops++;
            }

            ((IEngine) renderEngine).update();
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
