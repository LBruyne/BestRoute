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
 * @description: 高德输入提示相关服务封装
 */
@Slf4j
public class InputTipsUtil {

    /**
     * 高德地图API
     */
    private static final String GAODE_API_URL = "http://restapi.amap.com";

    /**
     * 输入提示URI前缀
     */
    private static final String inputTipsUriPrefix = "/v3/assistant/inputtips?";

    /**
     * 根据输入信息，返回提示信息查询结果
     *
     * @param key
     * @param keywords
     * @return
     */
    public static JSONObject inputTipsAPI(String key, String keywords) {
        String url = new InputTipsParams.InputTipsParamsBuilder()
                .key(key)
                .keywords(keywords)
                .build()
                .buildUrl();

        try {
            return JSON.parseObject(HttpUtil.httpGet(url));
        } catch (Exception e) {
            log.warn("输入提示信息请求失败，keywords={}, error={}", keywords, e.getMessage());
            return null;
        }
    }


    @Test
    public void TestInputTipsAPI() {
        JSONObject result = inputTipsAPI("34f6a43789a1aead9964cade6af6b9ad", "仙林");
        System.out.println(result);
    }

    @Data
    @Builder
    static class InputTipsParams {

        private String key;

        private String keywords;

        private String buildUrl() {
            StringBuilder sb = new StringBuilder();
            sb.append(InputTipsUtil.GAODE_API_URL).append(InputTipsUtil.inputTipsUriPrefix);

            if (null != key) {
                sb.append("key=").append(key);
            }

            if (null != keywords) {
                sb.append("&keywords=").append(keywords);
            }

            return sb.toString();
        }

    }

}
