package input.engine;

import input.sources.ISource;

public interface I_InputEngine {
    ISource addSource(ISource source);

    void removeSource(int sourceId);
}
