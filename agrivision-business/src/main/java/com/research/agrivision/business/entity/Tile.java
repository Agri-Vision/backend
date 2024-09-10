package com.research.agrivision.business.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tile extends BaseEntity {
    private Long id;
    private String tileImage;
    private String tileImageUrl;
    private String yield;
    private String stress;
    private String disease;
    private Double ndvi;
    private Double rendvi;
    private Double cire;
    private Double pri;
    private Double temperature;
    private Double humidity;
    private Double uvLevel;
    private Double soilMoisture;
    private Double pressure;
    private Double altitude;
}
