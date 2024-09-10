package com.research.agrivision.api.adapter.api.controller;

import com.research.agrivision.api.adapter.api.response.CommonResponse;
import com.research.agrivision.business.entity.Plantation;
import com.research.agrivision.business.entity.Tile;
import com.research.agrivision.business.port.in.*;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/prediction")
public class PredictionController {
    private final DiseaseUseCase diseaseService;
    private final YieldUseCase yieldService;
    private final StressUseCase stressService;
    private final ProjectUseCase projectService;
    private final OrganizationUseCase organizationService;

    private final ModelMapper modelMapper = new ModelMapper();

    public PredictionController(DiseaseUseCase predictionService, YieldUseCase yieldService, StressUseCase stressService, ProjectUseCase projectUseCase, OrganizationUseCase organizationService) {
        this.diseaseService = predictionService;
        this.yieldService = yieldService;
        this.stressService = stressService;
        this.projectService = projectUseCase;
        this.organizationService = organizationService;
    }

    @GetMapping("/disease/health-score/{tileId}")
    public ResponseEntity<CommonResponse> getDiseaseScoreByTileId(@PathVariable Long tileId) {
        Tile tile = projectService.getTileById(tileId);
        if (tile == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        String diseasePrediction = diseaseService.getDiseaseScoreByTileId(tile);
        return ResponseEntity.ok(new CommonResponse(diseasePrediction));
    }

    @GetMapping("/yield/{tileId}/{plantationId}")
    public ResponseEntity<CommonResponse> getYieldByTileId(@PathVariable Long tileId, @PathVariable Long plantationId) {
        Tile tile = projectService.getTileById(tileId);
        if (tile == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Plantation plantation = organizationService.getPlantationById(plantationId);
        if (plantation == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        String result = yieldService.validateTile(tile, plantation);
        return ResponseEntity.ok(new CommonResponse(result));
    }

    @GetMapping("/stress/{tileId}")
    public ResponseEntity<CommonResponse> getStressByTileId(@PathVariable Long tileId) {
        Tile tile = projectService.getTileById(tileId);
        if (tile == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        String result = stressService.getStressByTileId(tile);
        return ResponseEntity.ok(new CommonResponse(result));
    }
}
