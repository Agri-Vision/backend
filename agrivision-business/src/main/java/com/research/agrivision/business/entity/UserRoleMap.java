package com.research.agrivision.business.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleMap extends BaseEntity {
    private Long id;
    private User user;
    private UserRole role;
}
