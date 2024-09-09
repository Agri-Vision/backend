package com.research.agrivision.api.adapter.integration.integrate.sample.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModelResponse {
    public ArrayList<Integer> prediction;
}
