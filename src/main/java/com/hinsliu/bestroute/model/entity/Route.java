package com.hinsliu.bestroute.model.entity;

import com.hinsliu.bestroute.heuristic.ga.Chromosome;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: Hins Liu
 * @description: 路线
 */
public class Route implements Chromosome {

    private double[][] costs;

    private List<Integer> toVisit;

    private double cost;

    private double fitness;

    public Route(double[][] cost, Integer toVisitLength) {
        this.costs = cost;
        this.toVisit = new ArrayList<>();

        // random init
        Random random = new Random();
        Set<Integer> selected = new HashSet<>();
        while (true) {
            Integer next = random.nextInt(toVisitLength) + 1;
            if (selected.contains(next)) {
                continue;
            }

            toVisit.add(next);
            selected.add(next);
            if (geneSize() == toVisitLength) {
                break;
            }
        }
    }

    public Route(double[][] cost, List<Integer> toVisit) {
        this.costs = cost;
        this.toVisit = toVisit;
    }

    @Override
    public double getFitness() {
        return fitness;
    }

    @Override
    public List<Integer> getGene() {
        return toVisit;
    }

    @Override
    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @Override
    public void calcFitness() {
        List<Integer> realRoute = new ArrayList<>(toVisit);
        realRoute.add(0, 0);
        realRoute.add(realRoute.size(), 0);

        double totalCost = 0;
        for (int i = 0; i < realRoute.size() - 1; i++) {
            totalCost += costs[realRoute.get(i)][realRoute.get(i + 1)];
        }

        // fitness
        this.cost = totalCost;
        this.fitness = 1 / totalCost;
    }

    @Override
    public void mutate() {
        // random exchange
        if (toVisit != null) {
            Random random = new Random();
            Collections.swap(toVisit, random.nextInt(geneSize()), random.nextInt(geneSize()));
        }
    }

    @Override
    public int geneSize() {
        return toVisit.size();
    }

    @Override
    public List<Chromosome> cross(Chromosome other) {
        // random cross
        List<Integer> otherToVisit = other.getGene();
        if (toVisit != null && otherToVisit != null && toVisit.size() == otherToVisit.size()) {
            Random random = new Random();
            int pos1 = random.nextInt(geneSize());
            int pos2 = random.nextInt(geneSize());
            // pos1 < pos2
            if (pos1 == pos2) return null;
            else if (pos1 > pos2) {
                int tmp;
                tmp = pos2;
                pos2 = pos1;
                pos1 = tmp;
            }

            Route a =  new Route(costs, crossGenes(toVisit, otherToVisit, pos1, pos2));
            Route b =  new Route(costs, crossGenes(toVisit, otherToVisit, pos1, pos2));
            return Arrays.asList(a, b);
//            return Arrays.asList(
//                    new Route(costs, crossGenes(toVisit, otherToVisit, pos1, pos2)),
//                    new Route(costs, crossGenes(otherToVisit, toVisit, pos1, pos2))
//            );
        }
        return null;
    }

    public static List<Integer> crossGenes(List<Integer> father, List<Integer> mother, int lpos, int rpos) {
        if (father.size() != mother.size()) return null;
        List<Integer> slice = mother.subList(lpos, rpos + 1);
        List<Integer> child = Stream.of(father.subList(0, lpos), slice, father.subList(rpos + 1, father.size()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        while(true) {
            boolean hasRepeat = false;
            for (int i = 0; i < child.size(); i++) {
                if (i >= lpos && i <= rpos) continue;
                if (slice.contains(child.get(i))) {
                    hasRepeat = true;
                    child.set(i, father.subList(lpos, rpos + 1).get(slice.indexOf(child.get(i))));
                    break;
                }
            }

            if(!hasRepeat) break;
        }
        return child;
    }
}
