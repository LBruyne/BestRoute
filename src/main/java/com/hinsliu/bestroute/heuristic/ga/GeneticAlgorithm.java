package com.hinsliu.bestroute.heuristic.ga;

import lombok.Data;

/**
 * @author: Hins Liu
 * @description: 遗传算法实现
 */
@Data
public class GeneticAlgorithm {

    private Population population;

    private Integer populationMaxSize;

    private Integer iterNumber;

    private Double mutateRate = 0.1;

    private Double crossRate = 0.05;

    private Double eliteRate = 0.1;

    private int generation;

    private Chromosome bestChromosome;

    public Chromosome getBestChromosome() {
        return bestChromosome;
    }

    public void setBestChromosome(Chromosome bestChromosome) {
        this.bestChromosome = bestChromosome;
    }

    public GeneticAlgorithm(Population population, Integer populationMaxSize, Integer iterNumber, Double mutateRate, Double crossRate, Double eliteRate) {
        this.population = population;
        this.populationMaxSize = populationMaxSize;
        this.iterNumber = iterNumber;
        this.mutateRate = mutateRate;
        this.crossRate = crossRate;
        this.eliteRate = eliteRate;

        population.setConfig(populationMaxSize, mutateRate, crossRate, eliteRate);
    }

    public GeneticAlgorithm(Population population, Integer populationMaxSize, Integer iterNumber) {
        this.population = population;
        this.populationMaxSize = populationMaxSize;
        this.iterNumber = iterNumber;

        population.setConfig(populationMaxSize, mutateRate, crossRate, eliteRate);
    }

    public void train() {
        while (generation < iterNumber) {
            generation++;
            population = population.evolve();

            // 更新最佳个体
            if (bestChromosome == null || population.getBestChromosomeInPopulation().getFitness() > bestChromosome.getFitness()) {
                setBestChromosome(population.getBestChromosomeInPopulation());
            }
        }
    }
}
