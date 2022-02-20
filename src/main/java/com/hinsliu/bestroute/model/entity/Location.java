package com.hinsliu.bestroute.model.entity;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: Hins Liu
 * @description: 地理位置
 */
@Data
@Builder
public class Location {

    private String name;

    @NotNull
    private Double longitude;

    @NotNull
    private Double latitude;

    public String parsePair() {
        return String.format("%.6f", longitude) + "," + String.format("%.6f", latitude);
    }
}
