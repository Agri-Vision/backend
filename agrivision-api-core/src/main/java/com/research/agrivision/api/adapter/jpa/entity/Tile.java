package com.research.agrivision.api.adapter.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class Tile extends BaseEntity {
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
    private Integer row;
    private Integer col;
    private String rowCol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taskId")
    private Task task;
}
