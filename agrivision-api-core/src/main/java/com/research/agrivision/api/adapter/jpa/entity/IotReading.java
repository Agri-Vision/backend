package com.research.agrivision.api.adapter.jpa.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
public class IotReading extends BaseEntity {
    private BigDecimal temperature;
    private BigDecimal humidity;
    private BigDecimal uvLevel;
    private BigDecimal soilMoisture;
    private BigDecimal pressure;
    private BigDecimal altitude;
    private LocalTime recordedTime;
    private LocalDate recordedDate;
}
