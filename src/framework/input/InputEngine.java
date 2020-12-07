package framework.input;

import framework.input.sources.ISource;

import java.util.Vector;

/**
 * Input Engine
 */
public class InputEngine implements I_InputEngine {
    private final Vector<ISource> sources;

    public InputEngine() {
        this.sources = new Vector<>();
    }

    /**
     * Adds a new source to the InputEngine
     *
     * @param source new source
     * @return added source
     */
    public ISource addSource(ISource source) {
        sources.add(source);
        return source;
    }

    /**
     * Remove an existing source from the InputEngine
     *
     * @param source source to remove
     */
    public void removeSource(ISource source) {
        for (ISource s : sources) {
            if (s.getId() == source.getId()) {
                sources.remove(s);
                break;
            }
        }
    }

    /**
     * Updates all sources
     */
    public void update() {
        for (ISource source : sources) {
            source.update();
        }
    }
}
