package com.hinsliu.bestroute.model.input;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author: Hins Liu
 * @description: 查询的内容
 */
@Data
public class QueryForm {

    @NotBlank(message = "景点不能为空")
    private List<String> locations;

    @NotBlank(message = "起点不能为空")
    private String start;

    @NotBlank(message = "终点不能为空")
    private String end;

}
