package com.research.agrivision.api.adapter.integration.integrate.sample.request;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class DiseaseRequest {
    public ArrayList<Double> features;
}
