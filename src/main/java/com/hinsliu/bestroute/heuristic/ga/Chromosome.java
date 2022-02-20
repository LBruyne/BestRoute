package com.hinsliu.bestroute.heuristic.ga;

import java.util.List;

/**
 * @author: Hins Liu
 * @description: 遗传算法中的个体即染色体
 */
public interface Chromosome {

    double getFitness();

    double getCost();

    List<Integer> getGene();

    void calcFitness();

    void mutate();

    int geneSize();

    List<Chromosome> cross(Chromosome other);

}
