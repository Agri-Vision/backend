package com.research.agrivision.business.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Organization extends BaseEntity {
    private Long id;
    private String orgCode;
    private String orgName;
    private String district;
    private String orgImage;
    private String orgImageUrl;
    private List<Plantation> plantationList;
}
