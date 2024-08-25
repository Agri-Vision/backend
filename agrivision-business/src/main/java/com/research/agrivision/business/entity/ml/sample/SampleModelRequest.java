package com.research.agrivision.business.entity.ml.sample;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SampleModelRequest {
    public ArrayList<ArrayList<Double>> input_data;

    @Override
    public String toString() {
        return "SampleModelRequest{" +
                "input_data=" + input_data +
                '}';
    }
}
