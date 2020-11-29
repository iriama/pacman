package framework.input.sources;

import framework.utility.IdFactory;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Physical keyboard
 */
public class Keyboard implements ISource, KeyEventDispatcher {
    static Object _lock = new Object();
    private int id;
    private HashSet<Integer> pressed;
    private HashSet<Integer> released;
    private HashSet<Integer> newPressed;
    private HashSet<Integer> newReleased;

    private HashMap<Integer, IKeyboardEvent> pressedEvents;
    private HashMap<Integer, IKeyboardEvent> releasedEvents;

    private boolean active;

    public Keyboard() {
        active = true;
        pressed = new HashSet<Integer>();
        released = new HashSet<Integer>();
        newPressed = new HashSet<Integer>();
        newReleased = new HashSet<Integer>();

        id = IdFactory.nextId();
        this.pressedEvents = new HashMap<Integer, IKeyboardEvent>();
        this.releasedEvents = new HashMap<Integer, IKeyboardEvent>();
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
    }

    /**
     * Update the keyboard and fire events
     */
    public void update() {
        if (active) {
            for (int key : newPressed) {
                if (pressedEvents.containsKey(key)) pressedEvents.get(key).action();
            }

            for (int key : newReleased) {
                if (releasedEvents.containsKey(key)) releasedEvents.get(key).action();
            }
        }

        newPressed.clear();
        newReleased.clear();
    }


    /**
     * Map a key state to an action
     *
     * @param key   key code
     * @param state key state
     * @param event action
     */
    public void mapKey(int key, KeyStateEnum state, IKeyboardEvent event) {
        switch (state) {
            case PRESSED:
                pressedEvents.put(key, event);
                break;
            case RELEASED:
                releasedEvents.put(key, event);
                break;
        }
    }

    /**
     * Return the id of the keyboard
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Enable the keyboard
     */
    public void enable() {
        active = true;
    }

    /**
     * Disable the keyboard
     */
    public void disable() {
        active = false;
    }

    /**
     * Handle keys event
     *
     * @param e event
     * @return boolean
     */
    public boolean dispatchKeyEvent(KeyEvent e) {
        synchronized (_lock) {
            int key = e.getKeyCode();
            switch (e.getID()) {
                case KeyEvent.KEY_PRESSED:
                    if (!pressed.contains(key)) {
                        pressed.add(key);
                        newPressed.add(key);
                    }
                    if (released.contains(key)) {
                        released.remove(key);
                    }
                    break;

                case KeyEvent.KEY_RELEASED:
                    if (!released.contains(key)) {
                        released.add(key);
                        newReleased.add(key);
                    }
                    if (pressed.contains(e.getKeyCode())) {
                        pressed.remove(e.getKeyCode());
                    }
                    break;
            }


        }

        return false;
    }
}
