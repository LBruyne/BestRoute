package com.hinsliu.bestroute.controller;

import com.hinsliu.bestroute.model.dto.input.RouteSearchForm;
import com.hinsliu.bestroute.model.dto.output.RespBean;
import com.hinsliu.bestroute.model.dto.output.RouteSearchResp;
import com.hinsliu.bestroute.service.BestRouteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author: Hins Liu
 * @description: 路线接口
 */
@Slf4j
@RestController
@RequestMapping("/app/route")
public class RouteController {

    @Autowired
    BestRouteService bestRouteService;

    @PostMapping("/search")
    public RespBean search(@Valid @RequestBody RouteSearchForm form) {
        RouteSearchResp result = bestRouteService.planRoute(form);
        if (result == null) {
            return RespBean.fail("规划失败");
        } else {
            return RespBean.success(result);
        }
    }
}
