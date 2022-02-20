package com.hinsliu.bestroute.model.dto.input;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: Hins Liu
 * @description: 地理位置模糊搜索表单
 */
@Data
public class GeoSearchForm {

    @NotBlank(message = "输入信息不能为空")
    private String input;

}
