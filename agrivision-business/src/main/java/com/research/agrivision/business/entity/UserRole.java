package com.research.agrivision.business.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRole extends BaseEntity {
    private Long id;
    private String roleName;
    private String roleType;
}
