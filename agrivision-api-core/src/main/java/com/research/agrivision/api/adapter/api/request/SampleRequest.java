package com.research.agrivision.api.adapter.api.request;

import com.research.agrivision.business.enums.SampleType;
import com.research.hexa.core.Request;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class SampleRequest implements Request {
    private Long id;
    private String name;
    private String description;
    private boolean status;
    private BigDecimal amount;
    private SampleType type;
}
