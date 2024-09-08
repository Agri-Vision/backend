package com.research.agrivision.api.adapter.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AvUserRoleMap extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private AvUser user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private AvUserRole role;
}
