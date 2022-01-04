package com.hinsliu.bestroute.service;

import com.hinsliu.bestroute.model.input.QueryForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * @author: Hins Liu
 * @description: service for best route solution.
 */
@Slf4j
@Service
public class BestRouteService extends BaseService {

    public String getBestRoute(@Validated QueryForm query) {
        //


        return null;
    }
}
