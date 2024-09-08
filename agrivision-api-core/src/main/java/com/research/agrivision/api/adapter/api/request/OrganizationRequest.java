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
public class OrganizationRequest implements Request {
    private Long id;
    private String orgCode;
    private String orgName;
    private String district;
    private String orgImage;
}
