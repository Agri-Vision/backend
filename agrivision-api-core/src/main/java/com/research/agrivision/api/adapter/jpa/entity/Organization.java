package com.research.agrivision.api.adapter.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Organization extends BaseEntity {
    private String orgCode;
    private String orgName;
    private String district;
    private String orgImage;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "organizationId")
    private List<Plantation> plantationList;
}
