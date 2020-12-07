package framework.input;

import framework.input.sources.ISource;

/**
 * Input engine interface
 */
public interface I_InputEngine {
    ISource addSource(ISource source);

    void removeSource(ISource source);

    void update();
}
