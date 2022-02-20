package com.hinsliu.bestroute.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hinsliu.bestroute.heuristic.ga.Chromosome;
import com.hinsliu.bestroute.heuristic.ga.GeneticAlgorithm;
import com.hinsliu.bestroute.model.dto.input.RouteSearchForm;
import com.hinsliu.bestroute.model.dto.output.GeoSearchResp;
import com.hinsliu.bestroute.model.dto.output.RouteSearchResp;
import com.hinsliu.bestroute.model.entity.GeoLocation;
import com.hinsliu.bestroute.model.entity.Location;
import com.hinsliu.bestroute.model.entity.Routes;
import com.hinsliu.bestroute.utils.Constants;
import com.hinsliu.bestroute.utils.enums.RouteCostType;
import com.hinsliu.bestroute.utils.gaode.DirectionUtil;
import com.hinsliu.bestroute.utils.gaode.InputTipsUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: Hins Liu
 * @description: service for best route solution.
 */
@Slf4j
@Service
public class BestRouteService {

    /**
     * 高德地图开发者Key
     */
    @Value("${gaode.key}")
    private String key;

    public GeoSearchResp geoInfoFuzzySearch(String inputTip) {
        JSONObject obj = InputTipsUtil.inputTipsAPI(inputTip, key);
        if (obj == null || !obj.getBoolean("status")) {
            return null;
        }

        Integer count = obj.getInteger("count");
        JSONArray tips = obj.getJSONArray("tips");
        List<GeoLocation> results = new ArrayList<>();
        for(int i = 0; i < tips.size(); i++) {
            JSONObject tip = tips.getJSONObject(i);
            GeoLocation result = tip.toJavaObject(GeoLocation.class);
            result.parseLocation();
            results.add(result);
        }

        return GeoSearchResp.builder()
                .count(count)
                .tips(results)
                .build();
    }

    public RouteSearchResp planRoute(RouteSearchForm form) {
        List<Location> locationsWithStart = new ArrayList<>(form.getLocations());
        locationsWithStart.add(0, form.getStart());

        // get cost
        int length = locationsWithStart.size();
        double[][] cost = new double[length][length];
        for(int i = 0; i < length; i++) {
            for( int j = i + 1; j < length; j++) {
                double[] result = DirectionUtil.getDrivingDirectionCost("34f6a43789a1aead9964cade6af6b9ad", locationsWithStart.get(i), locationsWithStart.get(j), RouteCostType.DURATION);
                cost[i][j] = cost[j][i] = result == null ? Constants.INF : Arrays.stream(result).average().orElse(Constants.INF);
            }
        }

        // GA
        Routes routes = new Routes(cost, form.getLocations().size());
        GeneticAlgorithm ga = new GeneticAlgorithm(routes, 20, 10);
        ga.train();

        // get result
        Chromosome result = ga.getBestChromosome();
        List<Integer> seq = result.getGene();

        List<Location> visitSeq = new ArrayList<>();
        visitSeq.add(form.getStart());
        seq.forEach(i -> visitSeq.add(locationsWithStart.get(i)));
        return RouteSearchResp.builder()
                .route(visitSeq)
                .cost(result.getCost())
                .build();
    }

    @Test
    public void TestPlanRoute() {
        RouteSearchForm form = new RouteSearchForm();
        List<Location> locations = new ArrayList<>();
        Location loc;
        loc = Location.builder().longitude(116.465493).latitude(40.112749).build();
        locations.add(loc);
        loc = Location.builder().longitude(116.402252).latitude(39.998763).build();
        locations.add(loc);
        loc = Location.builder().longitude(116.613821).latitude(40.073008).build();
        locations.add(loc);
        loc = Location.builder().longitude(116.660964).latitude(40.214203).build();
        locations.add(loc);
        loc = Location.builder().longitude(116.808142).latitude(40.180696).build();
        locations.add(loc);

        form.setLocations(locations);
        form.setStart(Location.builder().longitude(116.148139).latitude(39.748914).build());

        RouteSearchResp resp = planRoute(form);
        System.out.println(resp.getCost());
    }
}
