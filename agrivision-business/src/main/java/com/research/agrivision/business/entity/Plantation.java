package com.research.agrivision.business.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Plantation extends BaseEntity {
    private Long id;
    private String plantationName;
    private String address;
    private String contactNo;
    private String plantationImg;
    private String plantationImgUrl;
    private Double currentLatitude;
    private Double currentLongitude;
}
