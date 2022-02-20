package com.hinsliu.bestroute.model.dto.input;

import com.hinsliu.bestroute.model.entity.Location;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author: Hins Liu
 * @description: 路线规划请求表单
 */
@Data
public class RouteSearchForm {

    @NotNull
    Location start;

    @NotEmpty(message = "地点不能为空")
    List<Location> locations;

}
