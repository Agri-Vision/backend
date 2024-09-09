package com.research.agrivision.api.adapter.api.controller;

import com.research.agrivision.business.entity.Project;
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
}
