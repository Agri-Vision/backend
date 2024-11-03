package com.research.agrivision.business.entity.imageTool.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pri {
    public double mean;
    public double median;
    public double min;
    public double max;
    public double std_dev;
}
