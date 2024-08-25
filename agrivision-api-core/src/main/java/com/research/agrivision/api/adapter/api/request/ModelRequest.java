package com.research.agrivision.api.adapter.api.request;

import com.research.hexa.core.Request;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ModelRequest implements Request {
    private Double pregnancies;
    private Double plasmaGlucose;
    private Double diastolicBloodPressure;
    private Double tricepsThickness;
    private Double serumInsulin;
    private Double bmi;
    private Double diabetesPedigree;
    private Double age;
}
