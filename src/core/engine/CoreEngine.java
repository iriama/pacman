package core.engine;

import core.IEngine;
import core.game.Character;
import core.game.ICharacter;
import core.utility.IdFactory;
import input.engine.I_InputEngine;
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
    private I_InputEngine inputEngine;
    private IEngine gameEngine;
    private Vector<ICharacter> characters;

    final int TICKS_PER_SECOND = 25;
    final int SKIP_TICKS = 1000 / TICKS_PER_SECOND;
    final int MAX_FRAMESKIP = 5;

    public CoreEngine(IRenderEngine renderEngine, IPhysicsEngine physicsEngine, I_InputEngine inputEngine, IEngine gameEngine) {
        this.renderEngine = renderEngine;
        this.physicsEngine = physicsEngine;
        this.inputEngine = inputEngine;
        this.gameEngine = gameEngine;
        this.characters = new Vector<ICharacter>();
    }

    /**
     * Update the core engine
     */
    public void update() {
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

                ((IEngine) inputEngine).update();
                ((IEngine) physicsEngine).update();
                update();
                gameEngine.update();

                next_game_tick += SKIP_TICKS;
                loops++;
            }

            ((IEngine) renderEngine).update();
        }
    }


    /**
     * Adds a character to the core engine
     * @param spriteSheetPath spriteSheetPath
     * @param spriteWidth spriteWidth
     * @param spriteCount spriteCount
     * @param x x
     * @param width width
     * @param y y
     * @param height height
     * @return added character
     */
    public ICharacter addCharacter(String spriteSheetPath, int spriteWidth, int spriteCount, int x, int width, int y, int height) {
        IGraphicObject graphicObject = new GraphicObject(
                new Sprite(
                        new SpriteSheet(spriteSheetPath, spriteWidth, spriteCount)
                ), IdFactory.nextId()
        );

        IPhyObject phyObject = new PhyObject(x, width, y, height, IdFactory.nextId());

        return addCharacter(graphicObject, phyObject);
    }

    /**
     * Adds a character to the core engine
     * @param graphicObject graphicObject
     * @param phyObject phyObject
     * @return added character
     */
    public ICharacter addCharacter(IGraphicObject graphicObject, IPhyObject phyObject) {
        ICharacter character = new Character(graphicObject, phyObject, IdFactory.nextId());

        return addCharacter(character);
    }

    /**
     * Adds a character to the core engine
     * @param character character
     * @return added character
     */
    public ICharacter addCharacter(ICharacter character) {
        characters.add(character);
        renderEngine.addObject(character.getGraphicObject());
        physicsEngine.addObject(character.getPhyObject());
        characters.add(character);

        return character;
    }

    /**
     * Remove character from the core engine
     * @param characterId character id
     */
    public void removeCharacter(int characterId) {
        // TODO : remove from other engines
        characters.remove(new Character(null, null, characterId));
    }
}
