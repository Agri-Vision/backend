package com.research.agrivision.api.adapter.api.controller;

import com.research.agrivision.api.adapter.api.response.CommonResponse;
import com.research.agrivision.business.entity.Tile;
import com.research.agrivision.business.port.in.DiseaseUseCase;
import com.research.agrivision.business.port.in.ProjectUseCase;
import com.research.agrivision.business.port.in.StressUseCase;
import com.research.agrivision.business.port.in.YieldUseCase;
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

    private final ModelMapper modelMapper = new ModelMapper();

    public PredictionController(DiseaseUseCase predictionService, YieldUseCase yieldService, StressUseCase stressService, ProjectUseCase projectUseCase) {
        this.diseaseService = predictionService;
        this.yieldService = yieldService;
        this.stressService = stressService;
        this.projectService = projectUseCase;
    }

    @GetMapping("/disease/health-score/{tileId}")
    public ResponseEntity<CommonResponse> getDiseaseScoreByTileId(@PathVariable Long tileId) {
        Tile tile = projectService.getTileById(tileId);
        if (tile == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        String diseasePrediction = diseaseService.getDiseaseScoreByTileId(tile);
        return ResponseEntity.ok(new CommonResponse(diseasePrediction));
    }

    @GetMapping("/yield/{tileId}")
    public ResponseEntity<CommonResponse> getYieldByTileId(@PathVariable Long tileId) {
//        Tile tile = projectService.getTileById(tileId);
//        if (tile == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//        String diseasePrediction = diseaseService.getDiseaseScoreByTileId(tile);
        return ResponseEntity.ok(new CommonResponse(""));
    }

    @GetMapping("/stress/{tileId}")
    public ResponseEntity<CommonResponse> getStressByTileId(@PathVariable Long tileId) {
//        Tile tile = projectService.getTileById(tileId);
//        if (tile == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//        String diseasePrediction = diseaseService.getDiseaseScoreByTileId(tile);
        return ResponseEntity.ok(new CommonResponse(""));
    }
}
