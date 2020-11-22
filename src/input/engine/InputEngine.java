package input.engine;

import core.IEngine;
import input.sources.ISource;

import java.util.Vector;

public class InputEngine implements I_InputEngine, IEngine {
    Vector<ISource> sources;

    public InputEngine() {
        this.sources = new Vector<ISource>();
    }

    public ISource addSource(ISource source) {
        sources.add(source);
        return source;
    }

    public void removeSource(int sourceId) {
        for (ISource source: sources) {
            if (source.getId() == sourceId) {
                sources.remove(source);
                break;
            }
        }
    }

    public void update() {
        for (ISource source: sources) {
            source.process();
        }
    }
}
