package com.hinsliu.bestroute.model.entity;

import lombok.Data;

/**
 * @author: Hins Liu
 * @description: 地理位置信息
 */
@Data
public class GeoLocation {

    private String id;

    private String district;

    private String adcode;

    private String location;

    private String address;

    private Double longitude;

    private Double latitude;

    public void parseLocation() {
        if (location != null) {
            String[] subs = location.split(",", 2);
            this.longitude = Double.parseDouble(subs[0]);
            this.latitude = Double.parseDouble(subs[1]);
        }
    }

}
