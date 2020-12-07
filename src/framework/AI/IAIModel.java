package framework.AI;

import framework.geometry.Point;

/**
 * AI Model interface
 */
public interface IAIModel {
    /**
     * Returns AI prediction
     *
     * @return target position
     */
    Point getPrediction();
}
