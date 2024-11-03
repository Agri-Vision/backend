package com.research.agrivision.api.adapter.integration.integrate.tool.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cire {
    public double mean;
    public double median;
    public double min;
    public double max;
    public double std_dev;
}
