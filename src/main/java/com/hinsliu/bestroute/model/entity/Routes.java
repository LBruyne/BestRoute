package com.hinsliu.bestroute.model.entity;

import com.hinsliu.bestroute.heuristic.ga.Chromosome;
import com.hinsliu.bestroute.heuristic.ga.Population;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author: Hins Liu
 * @description: 路线集合
 */
public class Routes implements Population {

    private List<Chromosome> routes;

    private Chromosome bestRoute;

    private double totalFitness;

    private int maxSize = 10;

    private double mutateRate = 0.1;

    private double crossRate = 0.05;

    private double eliteRate = 0.1;

    public Routes(double[][] cost, int size) {
        routes = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            Route route = new Route(cost, size);
            route.calcFitness();
            routes.add(route);
        }
        updateFitness();
    }

    public Routes() {
        routes = new ArrayList<>();
    }

    @Override
    public void setConfig(int maxSize, double mutate, double cross, double elite) {
        this.maxSize = maxSize;
        this.mutateRate = mutate;
        this.crossRate = cross;
        this.eliteRate = elite;
    }

    public Population evolve() {
        Routes nextRoutes = new Routes();

        // 保留优质个体
        routes.sort((o1, o2) -> Double.compare(o2.getFitness(), o1.getFitness()));
        nextRoutes.routes.addAll(this.routes.subList(0, (int) (this.routes.size() * eliteRate)));

        // 交叉
        while (nextRoutes.routes.size() < maxSize) {
             List<Chromosome> children = getRouteFromRoutes().cross(getRouteFromRoutes());
            if (children != null) {
                for (Chromosome route : children) {
                    nextRoutes.routes.add(route);
                    if (nextRoutes.routes.size() == maxSize) {
                        break;
                    }
                }
            }
        }

        // 突变
        for (Chromosome route : nextRoutes.routes) {
            if (Math.random() < mutateRate) {
                route.mutate();
            }
        }

        // 计算适应度并更新指标
        for (Chromosome route : nextRoutes.routes) {
            route.calcFitness();
            nextRoutes.totalFitness += route.getFitness();
            if (nextRoutes.bestRoute == null || route.getFitness() > nextRoutes.bestRoute.getFitness()) {
                nextRoutes.bestRoute = route;
            }
        }

        return nextRoutes;
    }

    @Override
    public Chromosome getBestChromosomeInPopulation() {
        return bestRoute;
    }

    @Override
    public void updateFitness() {
        totalFitness = 0;
        for (Chromosome route : routes) {
            route.calcFitness();
            totalFitness += route.getFitness();
        }
    }

    /**
     * 轮盘赌方法，
     * 根据适应度从当前种群随机选择一个个体
     *
     * @return
     */
    private Chromosome getRouteFromRoutes() {
        double slice = Math.random() * totalFitness, current = 0d;
        for (Chromosome route : routes) {
            current += route.getFitness();
            if (current > slice) {
                return route;
            }
        }
        return routes.get(routes.size() - 1);
    }
}
