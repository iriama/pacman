package framework.core;

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

    private IRenderEngine renderEngine;
    private IPhysicsEngine physicsEngine;
    private I_InputEngine inputEngine;
    private IGameEngine gameEngine;
    private Vector<Character> characters;

    public CoreEngine(IPanel panel, IGameEngine gameEngine) {
        renderEngine = new RenderEngine(panel);
        physicsEngine = new PhysicsEngine();
        inputEngine = new InputEngine();
        this.gameEngine = gameEngine;
        characters = new Vector<Character>();
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
        Thread render = new Thread(()->{
            long start, elapsed;
            while (true) {
                start = System.currentTimeMillis();
                renderEngine.update();
                elapsed = System.currentTimeMillis() - start;
                try {
                    Thread.sleep(Math.max(0, 1000/FPS - elapsed));
                } catch (InterruptedException e) { e.printStackTrace(); }
            }
        });

        Thread others = new Thread(() -> {
            int ticks;
            long start, elapsed;
            while(true) {
                ticks = 0;
                start = System.currentTimeMillis();
                while (ticks < TICKS) {
                    ticks++;
                    inputEngine.update();
                    physicsEngine.update();
                    update();
                    gameEngine.update();
                }
                elapsed = System.currentTimeMillis() - start;
                try {
                    Thread.sleep(Math.max(0, 10 - elapsed));
                } catch (InterruptedException e) { e.printStackTrace(); }
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

    public ISource addInputSource(ISource source) {
        return inputEngine.addSource(source);
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

    public void clear() {
        for(Character character: characters) {
            removeCharacter(character);
        }
    }
}
