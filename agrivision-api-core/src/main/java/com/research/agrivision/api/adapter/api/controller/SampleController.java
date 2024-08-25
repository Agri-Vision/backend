package com.research.agrivision.api.adapter.api.controller;

import com.research.agrivision.api.adapter.api.response.CommonResponse;
import com.research.agrivision.business.entity.Organization;
import com.research.agrivision.business.entity.Sample;
import com.research.agrivision.business.port.in.SampleUseCase;
import com.research.agrivision.api.adapter.api.mapper.SampleMapper;
import com.research.agrivision.api.adapter.api.request.SampleRequest;
import com.research.agrivision.api.adapter.api.response.SampleResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/test")
public class SampleController {
    private final SampleUseCase sampleService;
    private final ModelMapper modelMapper = new ModelMapper();
    private final com.research.hexa.core.ModelMapper<Sample, SampleRequest, SampleResponse> mapper = new SampleMapper();

    public SampleController(SampleUseCase sampleService) {
        this.sampleService = sampleService;
    }

    @PostMapping("/sample")
    public ResponseEntity<SampleResponse> createSample(@RequestBody final SampleRequest request) {
        if (request == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Sample sample = sampleService.createSample(mapper.requestToEntity(request));
        return ResponseEntity.ok(mapper.entityToResponse(sample));
    }

    @GetMapping("/sample/{id}")
    public ResponseEntity<SampleResponse> getSampleById(@PathVariable Long id) {
        Sample sample = sampleService.getSampleById(id);
        if (sample == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(mapper.entityToResponse(sample));
    }

    @GetMapping("/sample")
    public ResponseEntity<List<SampleResponse>> getAllSamples() {
        List<Sample> sampleList = sampleService.getAllSamples();
        return ResponseEntity.ok(mapper.entityToResponse(sampleList));
    }

    @PutMapping("/sample")
    public ResponseEntity<SampleResponse> updateSample(@RequestBody final SampleRequest request) {
        if (request == null || request.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Sample sample = sampleService.getSampleById(request.getId());

        if (sample == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Sample updatedSample = sampleService.updateSample(mapper.requestToEntity(request));
        return ResponseEntity.ok(mapper.entityToResponse(updatedSample));
    }

    @DeleteMapping("/sample/{id}")
    public ResponseEntity<CommonResponse> deleteSampleById(@PathVariable Long id) {
        Sample sample = sampleService.getSampleById(id);

        if (sample == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        sampleService.deleteSampleById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse("Successfully deleted sample with id: " + id));
    }
}
