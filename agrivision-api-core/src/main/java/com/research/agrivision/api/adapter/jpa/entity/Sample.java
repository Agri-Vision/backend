package com.research.agrivision.api.adapter.jpa.entity;

import com.research.agrivision.business.enums.SampleType;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class Sample extends BaseEntity {
    private String name;
    private String description;
    private boolean status;
    private BigDecimal amount;
    private SampleType type;
}
