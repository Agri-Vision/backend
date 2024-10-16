package com.research.agrivision.api.adapter.api.controller;

import com.research.agrivision.api.adapter.api.response.CommonResponse;
import com.research.agrivision.business.entity.Project;
import com.research.agrivision.business.entity.Task;
import com.research.agrivision.business.entity.Tile;
import com.research.agrivision.business.enums.ProjectStatus;
import com.research.agrivision.business.port.in.ProjectUseCase;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {
    private final ProjectUseCase projectService;
    private final ModelMapper modelMapper = new ModelMapper();

    public ProjectController(ProjectUseCase projectService) {
        this.projectService = projectService;
    }

    @PostMapping()
    public ResponseEntity<Project> createProject(@RequestBody final Project project) {
        if (project == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Project createdProject = projectService.createProject(project);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        Project project = projectService.getProjectById(id);
        if (project == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(project);
    }

    @GetMapping()
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projectList = projectService.getAllProjects();
        if (projectList == null || projectList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(projectList);
    }

    @GetMapping("/plantation/{id}")
    public ResponseEntity<List<Project>> getAllProjectsByPlantationId(@PathVariable Long id) {
        List<Project> projectList = projectService.getAllProjectsByPlantationId(id);
        if (projectList == null || projectList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(projectList);
    }

    @PutMapping()
    public ResponseEntity<Project> updateProject(@RequestBody final Project request) {
        if (request == null || request.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Project project = projectService.getProjectById(request.getId());

        if (project == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Project updatedProject = projectService.updateProject(request);
        return ResponseEntity.ok(updatedProject);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Project>> getAllProjectsByStatus(@PathVariable String status) {
        ProjectStatus projectStatus = ProjectStatus.fromString(status);

        if (projectStatus == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        List<Project> projectList = projectService.getAllProjectsByStatus(projectStatus);
        if (projectList == null || projectList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(projectList);
    }

    @GetMapping("/tiles")
    public ResponseEntity<List<Tile>> getAllTiles() {
        List<Tile> tileList = projectService.getAllTiles();
        if (tileList == null || tileList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(tileList);
    }

    @GetMapping("/rgb/task/{id}")
    public ResponseEntity<Task> getRgbTaskByProjectId(@PathVariable Long id) {
        Task task = projectService.getRgbTaskByProjectId(id);
        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(task);
    }

    @PutMapping("/task")
    public ResponseEntity<CommonResponse> updateProject(@RequestBody final Task request) {
        if (request == null || request.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Task task = projectService.getTaskById(request.getId());

        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        task.setMapImagePng(request.getMapImagePng());
        task.setLowerLat(request.getLowerLat());
        task.setUpperLat(request.getUpperLat());
        task.setLowerLng(request.getLowerLng());
        task.setUpperLng(request.getUpperLng());

        projectService.updateTask(task);
        return ResponseEntity.ok(new CommonResponse("Success"));
    }
}
