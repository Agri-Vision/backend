package com.research.agrivision.business.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IotReading extends BaseEntity {
    private Long id;
    private String temperature;
    private String humidity;
    private String uvLevel;
    private String soilMoisture;
    private String pressure;
    private String altitude;
    private LocalTime recordedTime;
    private LocalDate recordedDate;
}
