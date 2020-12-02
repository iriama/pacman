package framework.AI;

public interface IAIEngine {
    IAIController addController(IAIController controller);
    void removeController(IAIController controller);

    void update();
}
