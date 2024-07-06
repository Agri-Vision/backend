package com.research.agrivision.business.entity;

import com.research.agrivision.business.enums.SampleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Sample extends BaseEntity{
    private Long id;
    private String name;
    private String description;
    private boolean status;
    private BigDecimal amount;
    private SampleType type;
}
