package com.research.agrivision.api.adapter.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Plantation extends BaseEntity {
    private String plantationName;
    private String address;
    private String contactNo;
    private String plantationImg;
    private Double currentLatitude;
    private Double currentLongitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizationId")
    private Organization organization;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "plantationId")
    private List<Project> projectList;
}
