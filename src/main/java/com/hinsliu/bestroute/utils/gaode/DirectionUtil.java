package com.hinsliu.bestroute.utils.gaode;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hinsliu.bestroute.model.entity.Location;
import com.hinsliu.bestroute.utils.enums.RouteCostType;
import com.hinsliu.bestroute.utils.http.HttpUtil;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.hinsliu.bestroute.model.entity.Route.crossGenes;

/**
 * @author: Hins Liu
 * @description: 高德路线规划相关服务封装
 */
@Slf4j
public class DirectionUtil {

    /**
     * 高德地图API
     */
    private static final String GAODE_API_URL = "http://restapi.amap.com";

    /**
     * 路径规划2.0URI前缀
     */
    private static final String DirectionUriPrefix = "/v5/direction";

    public static JSONObject drivingDirectionAPI(String key, Location origin, Location destination) {
        return drivingDirectionAPI(key, origin, destination, null, null);
    }

    public static JSONObject drivingDirectionAPI(String key, Location origin, Location destination, RouteCostType costType) {
        return drivingDirectionAPI(key, origin, destination, null, costType);
    }

    public static JSONObject drivingDirectionAPI(String key, Location origin, Location destination, Integer strategy, RouteCostType costType) {
        DirectionParams params = new DirectionParams.DirectionParamsBuilder()
                .key(key)
                .origin(origin)
                .destination(destination)
                .strategy(strategy)
                .build();

        if (costType == RouteCostType.DURATION) {
            List<String> showFields = new ArrayList<>();
            showFields.add("cost");
            params.setShowFields(showFields);
        }

        String url = params.buildUrl();

        try {
            return JSON.parseObject(HttpUtil.httpGet(url));
        } catch (Exception e) {
            log.warn("导航规划请求失败，type={}, origin=({},{}), destination=({},{}), strategy={}, error={}",
                    "driving", origin.getLongitude(), origin.getLatitude(), destination.getLongitude(), destination.getLatitude(), strategy, e.getMessage());
            return null;
        }
    }

    public static double[] getDrivingDirectionCost(String key, Location origin, Location destination, RouteCostType costType) {
        JSONObject apiResult = drivingDirectionAPI(key, origin, destination, costType);
        if (apiResult == null || !apiResult.getBoolean("status")) {
            return null;
        }

        double[] costs = new double[apiResult.getInteger("count")];
        JSONArray paths = apiResult.getJSONObject("route").getJSONArray("paths");
        for(int i = 0; i < paths.size(); i++) {
            JSONObject path = paths.getJSONObject(i);
            if(costType == RouteCostType.DURATION) {
                costs[i] = path.getJSONObject("cost").getDouble("duration");
            } else {
                costs[i] = path.getDouble("distance");
            }
        }
        return costs;
    }

    @Test
    public void TestDrivingDirectionAPI() {
        Location origin = Location.builder().longitude(118.71).latitude(32.12).build();
        Location destination = Location.builder().longitude(120.17).latitude(30.26).build();

        JSONObject result = drivingDirectionAPI("34f6a43789a1aead9964cade6af6b9ad", origin, destination, null, RouteCostType.DURATION);
        System.out.println(result);
    }

    @Test
    public void TestGetCost() {
        Location origin = Location.builder().longitude(118.71).latitude(32.12).build();
        Location destination = Location.builder().longitude(120.17).latitude(30.26).build();

        double[] costs = getDrivingDirectionCost("34f6a43789a1aead9964cade6af6b9ad", origin, destination, RouteCostType.DURATION);
        System.out.println(costs.length);
    }

    @Data
    @Builder
    static class DirectionParams {

        private String key;

        private Location origin;

        private Location destination;

        private Integer strategy;

        private List<String> showFields;

        private String buildUrl() {
            StringBuilder sb = new StringBuilder();
            sb.append(DirectionUtil.GAODE_API_URL).append(DirectionUtil.DirectionUriPrefix).append("/driving?");

            if (null != key) {
                sb.append("key=").append(key);
            }

            if (null != origin) {
                sb.append("&origin=").append(origin.parsePair());
            }

            if (null != destination) {
                sb.append("&destination=").append(destination.parsePair());
            }

            if (null != strategy) {
                sb.append("&strategy=").append(strategy);
            }

            if (null != showFields) {
                sb.append("&show_fields=").append(String.join(",", showFields));
            }

            return sb.toString();
        }

    }

}
