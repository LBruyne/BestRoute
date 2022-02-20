package com.hinsliu.bestroute.heuristic.ga;

import java.util.Comparator;
import java.util.List;

/**
 * @author: Hins Liu
 * @description:
 */
public interface Population {

    void setConfig(int maxSize, double mutate, double cross, double elite);

    Population evolve();

    Chromosome getBestChromosomeInPopulation();

    void updateFitness();

}
