package com.hinsliu.bestroute.model.dto.output;

import com.hinsliu.bestroute.model.entity.GeoLocation;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author: Hins Liu
 * @description: 地理信息查询响应
 */
@Data
@Builder
public class GeoSearchResp {

    private List<GeoLocation> tips;

    private Integer count;

}
