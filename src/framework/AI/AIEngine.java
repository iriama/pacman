package framework.AI;

import java.util.Vector;

public class AIEngine implements IAIEngine {
    Vector<IAIController> controllers;

    public AIEngine() {
        controllers = new Vector<>();
    }

    public IAIController addController(IAIController controller) {
        controllers.add(controller);
        return controller;
    }

    public void removeController(IAIController controller) {
        controllers.remove(controller);
    }

    public void update() {
        for (IAIController controller : controllers)
            controller.update();
    }
}
