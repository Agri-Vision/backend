package com.research.agrivision.api.adapter.api.controller;

import com.research.agrivision.api.adapter.api.response.CommonResponse;
import com.research.agrivision.business.entity.Project;
import com.research.agrivision.business.entity.Task;
import com.research.agrivision.business.entity.imageTool.ToolReadings;
import com.research.agrivision.business.port.in.ProjectUseCase;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tool")
public class ImageToolController {
    private final ProjectUseCase projectService;

    private final ModelMapper modelMapper = new ModelMapper();

    public ImageToolController(ProjectUseCase projectService) {
        this.projectService = projectService;
    }

    @PostMapping()
    public ResponseEntity<CommonResponse> getImageIndexes(@RequestBody final ToolReadings request) {
        if (request == null || request.getProjectId() == null || request.getTaskId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Project project = projectService.getProjectByWebOdmProjectId(request.getProjectId());

        if (project == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CommonResponse("Project Not Found"));
        }

        Task task = projectService.getTaskByWebOdmTaskId(request.getTaskId());

        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CommonResponse("Task Not Found"));
        }

        projectService.createTile(request);

        return ResponseEntity.ok(new CommonResponse("Success"));
    }
}
