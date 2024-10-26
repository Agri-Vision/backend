package com.research.agrivision.api.adapter.api.controller;

import com.research.agrivision.api.adapter.api.response.CommonResponse;
import com.research.agrivision.business.port.in.ProjectUseCase;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    private final ProjectUseCase projectService;
    private final ModelMapper modelMapper = new ModelMapper();

    public DashboardController(ProjectUseCase projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/total/yield")
    public ResponseEntity<CommonResponse> getTotalYield() {
        String totalYield = projectService.getTotalYield();
        return ResponseEntity.ok(new CommonResponse(totalYield));
    }

    @GetMapping("/total/stress-pct")
    public ResponseEntity<CommonResponse> getTotalStressPct() {
        String totalStressPct = projectService.getTotalStressPct();
        return ResponseEntity.ok(new CommonResponse(totalStressPct));
    }

    @GetMapping("/total/disease-pct")
    public ResponseEntity<CommonResponse> getTotalDiseasePct() {
        String totalDiseasePct = projectService.getTotalDiseasePct();
        return ResponseEntity.ok(new CommonResponse(totalDiseasePct));
    }
}
