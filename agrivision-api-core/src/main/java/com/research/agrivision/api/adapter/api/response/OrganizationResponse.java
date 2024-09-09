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
public class OrganizationResponse implements Response {
    private Long id;
    private String orgCode;
    private String orgName;
    private String district;
    private String orgImage;
    private String orgImageUrl;
}
