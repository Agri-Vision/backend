package com.research.agrivision.business.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User extends BaseEntity {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String mobileNo;
    private String identityId;
    private boolean internal;
    private boolean status;
    private UserRole role;
}
