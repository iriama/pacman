package framework.core;

import framework.AI.AIEngine;
import framework.AI.IAIController;
import framework.AI.IAIEngine;
import framework.IGameEngine;
import framework.input.I_InputEngine;
import framework.input.InputEngine;
import framework.input.sources.ISource;
import framework.physics.IPhysicsEngine;
import framework.physics.PhyObject;
import framework.physics.PhysicsEngine;
import framework.rendering.GraphicObject;
import framework.rendering.IPanel;
import framework.rendering.IRenderEngine;
import framework.rendering.RenderEngine;
import framework.utility.IdFactory;

import java.awt.*;
import java.util.Vector;

public class CoreEngine implements ICoreEngine, Runnable {

    final int TICKS = 50;
    final int FPS = 60;
    private final IRenderEngine renderEngine;
    private final IPhysicsEngine physicsEngine;
    private final I_InputEngine inputEngine;
    private final IGameEngine gameEngine;
    private final IAIEngine aiEngine;
    private final Vector<Character> characters;

    public CoreEngine(IPanel panel, IGameEngine gameEngine) {
        renderEngine = new RenderEngine(panel);
        physicsEngine = new PhysicsEngine();
        inputEngine = new InputEngine();
        aiEngine = new AIEngine();
        this.gameEngine = gameEngine;
        characters = new Vector<>();
    }

    /**
     * Creates a Character
     *
     * @param graphicObject graphic Object
     * @param phyObject     physical Object
     * @return Character
     */
    public static Character createCharacter(GraphicObject graphicObject, PhyObject phyObject) {

        return new Character(graphicObject, phyObject, IdFactory.nextId());
    }

    /**
     * Update the framework.core engine
     */
    public void update() {
        for (Character character : characters) {
            if (character.getPhyObject() == null || character.getGraphicObject() == null) continue;

            PhyObject phyObject = character.getPhyObject();
            character.getGraphicObject().setPosition(phyObject.getX(), phyObject.getY());
        }
    }

    @Override
    public void run() {
        Thread render = new Thread(() -> {
            long start, elapsed;
            while (true) {
                start = System.currentTimeMillis();
                renderEngine.update();
                elapsed = System.currentTimeMillis() - start;
                try {
                    //noinspection BusyWait
                    Thread.sleep(Math.max(0, 1000 / FPS - elapsed));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread others = new Thread(() -> {
            int ticks;
            long start, elapsed;
            while (true) {
                ticks = 0;
                start = System.currentTimeMillis();
                while (ticks < TICKS) {
                    ticks++;
                    inputEngine.update();
                    physicsEngine.update();
                    aiEngine.update();
                    update();
                    gameEngine.update();
                }
                elapsed = System.currentTimeMillis() - start;
                try {
                    //noinspection BusyWait
                    Thread.sleep(Math.max(0, 10 - elapsed));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        render.start();
        others.start();
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
     * Adds an input source
     *
     * @param source source
     * @return added source
     */
    public ISource addInputSource(ISource source) {
        return inputEngine.addSource(source);
    }

    /**
     * Adds an AI controller
     *
     * @param controller controller
     * @return added controller
     */
    public IAIController addAIController(IAIController controller) {
        return aiEngine.addController(controller);
    }

    /**
     * Adds a character to the framework.core engine
     *
     * @param character character
     * @return added character
     */
    public Character addCharacter(Character character) {
        characters.add(character);
        if (character.getGraphicObject() != null)
            renderEngine.addObject(character.getGraphicObject());
        if (character.getPhyObject() != null)
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
        if (character.getPhyObject() != null)
            physicsEngine.removeObject(character.getPhyObject());
        if (character.getGraphicObject() != null)
            renderEngine.removeObject(character.getGraphicObject());

        characters.remove(character);
    }

    public void renderDraw(Graphics2D g) {
        renderEngine.draw(g);
    }

    /**
     * Remove all characters
     */
    public void clear() {
        for (Character character : characters) {
            removeCharacter(character);
        }

    }
}
