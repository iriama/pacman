package input.sources;

import core.utility.IdFactory;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.HashSet;

public class Keyboard implements ISource, KeyEventDispatcher {
    private int id;
    static Object _lock = new Object();
    private HashMap<Integer, Boolean> keyStates;
    private IKeyboardEvent listener;
    private HashSet<Integer> pressed;
    private HashSet<Integer> released;

    public Keyboard(IKeyboardEvent listener) {
        this.listener = listener;
        pressed = new HashSet<Integer>();
        released = new HashSet<Integer>();
        keyStates = new HashMap<Integer, Boolean>();
        id = IdFactory.nextId();
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
    }

    public boolean isKeyDown(int key) {
        return keyStates.containsKey(key) && keyStates.get(key);
    }

    public void process() { }

    public int getId() {
        return id;
    }

    public boolean dispatchKeyEvent(KeyEvent e) {
        synchronized (_lock) {
            switch (e.getID()) {
                case KeyEvent.KEY_PRESSED:
                    keyStates.put(e.getKeyCode(), true);
                    if (!pressed.contains(e.getKeyCode())) {
                        pressed.add(e.getKeyCode());
                        listener.onKey(e.getKeyCode(), true);
                    }
                    if (released.contains(e.getKeyCode())) {
                        released.remove(e.getKeyCode());
                    }
                    break;

                case KeyEvent.KEY_RELEASED:
                    keyStates.put(e.getKeyCode(), false);
                    if (!released.contains(e.getKeyCode())) {
                        released.add(e.getKeyCode());
                        listener.onKey(e.getKeyCode(), false);
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
