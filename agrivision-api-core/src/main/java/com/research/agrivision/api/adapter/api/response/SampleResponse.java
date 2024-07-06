package com.research.agrivision.api.adapter.api.response;

import com.research.agrivision.business.enums.SampleType;
import com.research.hexa.core.Response;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class SampleResponse implements Response {
    private Long id;
    private String name;
    private String description;
    private boolean status;
    private BigDecimal amount;
    private SampleType type;
}
