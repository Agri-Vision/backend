package com.research.agrivision.api.adapter.api.controller;

import com.research.agrivision.api.adapter.api.request.ModelRequest;
import com.research.agrivision.api.adapter.api.response.CommonResponse;
import com.research.agrivision.business.entity.ml.sample.DiseaseRequest;
import com.research.agrivision.business.entity.ml.sample.SampleModelRequest;
import com.research.agrivision.business.port.in.MlUseCase;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/ml")
public class MlController {
    @Value("${client.model-token}")
    private String authToken;

    private final MlUseCase mlService;

    private final ModelMapper mapper = new ModelMapper();

    public MlController(MlUseCase mlService) {
        this.mlService = mlService;
    }

    @PostMapping("/score")
    public ResponseEntity<String> getSampleModel(@RequestBody ModelRequest modelRequest) {
        ArrayList<Double> inputValues = new ArrayList<>();

        inputValues.add(modelRequest.getPregnancies() != null ? modelRequest.getPregnancies() : 0.0);
        inputValues.add(modelRequest.getPlasmaGlucose() != null ? modelRequest.getPlasmaGlucose() : 0.0);
        inputValues.add(modelRequest.getDiastolicBloodPressure() != null ? modelRequest.getDiastolicBloodPressure() : 0.0);
        inputValues.add(modelRequest.getTricepsThickness() != null ? modelRequest.getTricepsThickness() : 0.0);
        inputValues.add(modelRequest.getSerumInsulin() != null ? modelRequest.getSerumInsulin() : 0.0);
        inputValues.add(modelRequest.getBmi() != null ? modelRequest.getBmi() : 0.0);
        inputValues.add(modelRequest.getDiabetesPedigree() != null ? modelRequest.getDiabetesPedigree() : 0.0);
        inputValues.add(modelRequest.getAge() != null ? modelRequest.getAge() : 0.0);

        ArrayList<ArrayList<Double>> request = new ArrayList<>();
        request.add(inputValues);

        SampleModelRequest sampleModelRequest = new SampleModelRequest(request);
        String response = mlService.getSampleModel(sampleModelRequest, authToken);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/disease")
    public ResponseEntity<CommonResponse> getDiseaseModel(@RequestBody DiseaseRequest diseaseRequest) {
        String response = mlService.getDiseaseModel(diseaseRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse(response));
    }

    @PostMapping("/stress")
    public ResponseEntity<CommonResponse> getStressModel(@RequestBody DiseaseRequest diseaseRequest) {
        String response = mlService.getStressModel(diseaseRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse(response));
    }

    @PostMapping("/yield")
    public ResponseEntity<CommonResponse> getYieldModel(@RequestBody DiseaseRequest diseaseRequest) {
        String response = mlService.getYieldModel(diseaseRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse(response));
    }
}
