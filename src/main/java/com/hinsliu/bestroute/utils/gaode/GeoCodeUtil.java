package com.hinsliu.bestroute.utils.gaode;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hinsliu.bestroute.utils.http.HttpUtil;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author: Hins Liu
 * @description: 高德地址编码相关服务封装
 */
@Slf4j
public class GeoCodeUtil {

    /**
     * 高德地图API
     */
    private static final String GAODE_API_URL = "http://restapi.amap.com";

    /**
     * 地址编码URI前缀
     */
    private static final String geoCodeUriPrefix = "/v3/geocode/geo?";

    /**
     * 根据单个结构化地址，返回查询信息
     *
     * @param key
     * @param address
     * @return
     */
    public static JSONObject geoCodeAPI(String key, String address) {
        return geoCodeAPI(key, address, null);
    }

    /**
     * 根据单个结构化地址和所在城市，返回查询信息
     *
     * @param key
     * @param address
     * @param city
     * @return
     */
    public static JSONObject geoCodeAPI(String key, String address, String city) {
        String url = new GeoCodeParams.GeoCodeParamsBuilder()
                .key(key)
                .address(address)
                .city(city)
                .build()
                .buildUrl();

        try {
            return JSON.parseObject(HttpUtil.httpGet(url));
        } catch (Exception e) {
            log.warn("地址编码请求失败，address={}, city={}, error={}", address, city, e.getMessage());
            return null;
        }
    }

    @Data
    @Builder
    static class GeoCodeParams {

        private String key;

        private String address;

        private String city;

        private String buildUrl() {
            StringBuilder sb = new StringBuilder();
            sb.append(GeoCodeUtil.GAODE_API_URL).append(GeoCodeUtil.geoCodeUriPrefix);

            if (null != key) {
                sb.append("key=").append(key);
            }

            if (null != address) {
                sb.append("&address=").append(address);
            }

            if (null != city) {
                sb.append("&city=").append(city);
            }

            return sb.toString();
        }

    }

}
