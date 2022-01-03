package com.hinsliu.bestroute.model.input;

import lombok.Data;

import java.util.List;

/**
 * @author: Hins Liu
 * @description: 查询的内容
 */
@Data
public class QueryContent {

    private List<String> locations;

    private String start;

    private String end;

}
