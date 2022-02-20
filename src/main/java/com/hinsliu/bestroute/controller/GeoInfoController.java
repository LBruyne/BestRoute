package com.hinsliu.bestroute.controller;

import com.hinsliu.bestroute.model.dto.input.GeoSearchForm;
import com.hinsliu.bestroute.model.dto.output.GeoSearchResp;
import com.hinsliu.bestroute.model.dto.output.RespBean;
import com.hinsliu.bestroute.service.BestRouteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author: Hins Liu
 * @description: 地理信息接口
 */
@Slf4j
@RestController
@RequestMapping("/app/geo")
public class GeoInfoController {

    @Autowired
    BestRouteService bestRouteService;

    @PostMapping("/search")
    public RespBean search(@Valid @RequestBody GeoSearchForm form) {
        GeoSearchResp result = bestRouteService.geoInfoFuzzySearch(form.getInput());
        if(result == null) {
            return RespBean.fail("搜索失败");
        } else {
            return RespBean.success(result);
        }
    }
}
