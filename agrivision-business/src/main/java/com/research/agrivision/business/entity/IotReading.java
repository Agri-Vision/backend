package com.research.agrivision.business.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String iotId;
    private String temperature;
    private String humidity;
    private String uvLevel;
    private String soilMoisture;
    private String pressure;
    private String altitude;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime recordedTime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate recordedDate;
}
