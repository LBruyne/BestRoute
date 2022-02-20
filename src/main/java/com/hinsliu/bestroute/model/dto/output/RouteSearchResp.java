package com.hinsliu.bestroute.model.dto.output;

import com.hinsliu.bestroute.model.entity.Location;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author: Hins Liu
 * @description: 路线规划结果
 */
@Data
@Builder
public class RouteSearchResp {

    List<Location> route;

    double cost;

    double distance;

}
