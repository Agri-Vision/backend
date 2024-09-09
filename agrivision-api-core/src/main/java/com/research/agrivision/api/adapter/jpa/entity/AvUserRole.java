package com.research.agrivision.api.adapter.jpa.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AvUserRole extends BaseEntity {
    private String roleName;
    private String roleType;
}
