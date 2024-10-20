package com.research.agrivision.api.adapter.api.controller;

import com.research.agrivision.business.entity.Plantation;
import com.research.agrivision.business.port.in.PlantationUseCase;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/plantation")
public class PlantationController {
    private final PlantationUseCase plantationService;
    private final ModelMapper modelMapper = new ModelMapper();

    public PlantationController(PlantationUseCase plantationService) {
        this.plantationService = plantationService;
    }

    @GetMapping()
    public ResponseEntity<List<Plantation>> getAllPlantations() {
        List<Plantation> plantationList = plantationService.getAllPlantations();
        if (plantationList == null || plantationList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(plantationList);
    }
}
