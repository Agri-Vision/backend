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
    private String yield;
    private String stress;
    private String disease;
    private Double ndvi;
    private Double rendvi;
    private Double cire;
    private Double pri;
    private String temperature;
    private String humidity;
    private String uvLevel;
    private String soilMoisture;
    private String pressure;
    private String altitude;
}
