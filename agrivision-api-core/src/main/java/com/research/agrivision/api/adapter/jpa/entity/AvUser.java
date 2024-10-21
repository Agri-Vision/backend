package com.research.agrivision.api.adapter.jpa.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AvUser extends BaseEntity {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String mobileNo;
    private String identityId;
    private boolean internal;
    private boolean status;
    private String profileImg;
    private String profileImgUrl;
    private Double currentLatitude;
    private Double currentLongitude;
}
