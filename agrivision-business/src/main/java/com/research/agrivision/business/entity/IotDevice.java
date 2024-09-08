package com.research.agrivision.business.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IotDevice extends BaseEntity {
    private Long id;
    private String deviceCode;
    private Double currentLatitude;
    private Double currentLongitude;
}
