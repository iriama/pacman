package framework.core;

import framework.IGameEngine;
import framework.input.I_InputEngine;
import framework.physics.IPhysicsEngine;
import framework.physics.PhyObject;
import framework.rendering.GraphicObject;
import framework.rendering.IRenderEngine;
import framework.rendering.graphics.Sprite;
import framework.rendering.graphics.SpriteSheet;
import framework.utility.IdFactory;

import java.util.Vector;

public class CoreEngine implements ICoreEngine, Runnable {

    final int TICKS_PER_SECOND = 25;
    final int SKIP_TICKS = 1000 / TICKS_PER_SECOND;
    final int MAX_FRAMESKIP = 5;
    private IRenderEngine renderEngine;
    private IPhysicsEngine physicsEngine;
    private I_InputEngine inputEngine;
    private IGameEngine gameEngine;
    private Vector<Character> characters;

    public CoreEngine(IRenderEngine renderEngine, IPhysicsEngine physicsEngine, I_InputEngine inputEngine, IGameEngine gameEngine) {
        this.renderEngine = renderEngine;
        this.physicsEngine = physicsEngine;
        this.inputEngine = inputEngine;
        this.gameEngine = gameEngine;
        this.characters = new Vector<Character>();
    }

    /**
     * Update the framework.core engine
     */
    public void update() {
        for (Character character : characters) {
            PhyObject phyObject = character.getPhyObject();
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

                inputEngine.update();
                physicsEngine.update();
                update();
                gameEngine.update();

                next_game_tick += SKIP_TICKS;
                loops++;
            }

            renderEngine.update();
        }
    }


    /**
     * Adds a character to the framework.core engine
     *
     * @param spriteSheetPath spriteSheetPath
     * @param spriteWidth     spriteWidth
     * @param spriteCount     spriteCount
     * @param x               x
     * @param width           width
     * @param y               y
     * @param height          height
     * @return added character
     */
    public Character addCharacter(String spriteSheetPath, int spriteWidth, int spriteCount, int x, int width, int y, int height) {
        GraphicObject graphicObject = new GraphicObject(
                new Sprite(
                        new SpriteSheet(spriteSheetPath, spriteWidth, spriteCount)
                ), IdFactory.nextId()
        );

        PhyObject phyObject = new PhyObject(x, width, y, height, IdFactory.nextId());

        return addCharacter(graphicObject, phyObject);
    }

    /**
     * Adds a character to the framework.core engine
     *
     * @param graphicObject graphicObject
     * @param phyObject     phyObject
     * @return added character
     */
    public Character addCharacter(GraphicObject graphicObject, PhyObject phyObject) {
        Character character = new Character(graphicObject, phyObject, IdFactory.nextId());

        return addCharacter(character);
    }

    /**
     * Adds a character to the framework.core engine
     *
     * @param character character
     * @return added character
     */
    public Character addCharacter(Character character) {
        characters.add(character);
        renderEngine.addObject(character.getGraphicObject());
        physicsEngine.addObject(character.getPhyObject());
        characters.add(character);

        return character;
    }

    /**
     * Remove character from the framework.core engine
     *
     * @param character character
     */
    public void removeCharacter(Character character) {
        physicsEngine.removeObject(character.getPhyObject());
        renderEngine.removeObject(character.getGraphicObject());
        characters.remove(character);
    }
}
