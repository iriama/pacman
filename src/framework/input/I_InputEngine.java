package framework.input;

import framework.input.sources.ISource;

public interface I_InputEngine {
    ISource addSource(ISource source);

    void removeSource(ISource source);

    void update();
}
