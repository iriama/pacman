package framework.AI;

/**
 * AI engine interface
 */
public interface IAIEngine {
    /**
     * Adds an AI controller
     *
     * @param controller IAIController
     * @return added controller
     */
    IAIController addController(IAIController controller);

    /**
     * Removes an AI controller
     *
     * @param controller controller to remove
     */
    void removeController(IAIController controller);

    /**
     * Update all controllers
     */
    void update();
}
