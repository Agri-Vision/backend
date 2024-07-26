package com.research.agrivision.api.adapter.jpa.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Organization extends BaseEntity {
    private String orgCode;
    private String orgName;
    private String district;
}
