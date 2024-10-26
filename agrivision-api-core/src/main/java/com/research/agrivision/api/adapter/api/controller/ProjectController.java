package com.research.agrivision.api.adapter.api.controller;

import com.research.agrivision.api.adapter.api.response.CommonResponse;
import com.research.agrivision.business.entity.*;
import com.research.agrivision.business.entity.imageTool.Request.CreateProjectRequest;
import com.research.agrivision.business.entity.imageTool.Response.CreateProjectResponse;
import com.research.agrivision.business.entity.imageTool.Response.StartProjectResponse;
import com.research.agrivision.business.entity.project.ProjectHistory;
import com.research.agrivision.business.enums.ProjectStatus;
import com.research.agrivision.business.enums.ToolTaskStatus;
import com.research.agrivision.business.port.in.*;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/project")
public class ProjectController {
    private final ProjectUseCase projectService;
    private final UserManagementUseCase userService;
    private final ToolUseCase toolService;
    private final OrganizationUseCase organizationService;

    private final ModelMapper modelMapper = new ModelMapper();

    public ProjectController(ProjectUseCase projectService, UserManagementUseCase userService, ToolUseCase toolService, OrganizationUseCase organizationService) {
        this.projectService = projectService;
        this.userService = userService;
        this.toolService = toolService;
        this.organizationService = organizationService;
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

    @PutMapping("/maps/{id}")
    public ResponseEntity<CommonResponse> updateProjectMaps(
            @PathVariable Long id,
            @RequestParam(value = "rgbMap", required = false) MultipartFile rgbMap,
            @RequestParam(value = "rMap", required = false) MultipartFile rMap,
            @RequestParam(value = "gMap", required = false) MultipartFile gMap,
            @RequestParam(value = "reMap", required = false) MultipartFile reMap,
            @RequestParam(value = "nirMap", required = false) MultipartFile nirMap) {

        if (rgbMap == null && rMap == null && gMap == null && reMap == null && nirMap == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CommonResponse("Please upload at least one map"));
        }

        if (rgbMap == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CommonResponse("Please upload rgb map"));
        }

        Project project = projectService.getProjectById(id);

        if (project == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        projectService.updateProjectMaps(id, rgbMap);

        try {
            Task rgbTask = projectService.getRgbTaskByProjectId(id);

            CreateProjectRequest projectRequest = new CreateProjectRequest(UUID.randomUUID().toString(), UUID.randomUUID().toString());
            CreateProjectResponse projectResponse = toolService.createProject(projectRequest);

            //TODO create jpg image from rgb map
            List<MultipartFile> files = new ArrayList<>();
            MultipartFile jpgRgbFile = convertTiffToJpg(rgbMap);

            files.add(jpgRgbFile);
            files.add(rMap);
            files.add(gMap);
            files.add(reMap);
            files.add(nirMap);

            MultipartFile[] filesArray = files.toArray(new MultipartFile[0]);
            toolService.projectFileUpload(projectResponse.getProject_id(), filesArray);

            StartProjectResponse startProjectResponse = toolService.startProject(projectResponse.getProject_id());

            ToolProject toolProject = new ToolProject();
            toolProject.setToolProjectId(projectResponse.getProject_id());
            toolProject.setToolTaskId(startProjectResponse.getTask_id());
            toolProject.setTask(rgbTask);
            toolProject.setStatus(ToolTaskStatus.PENDING);

            toolService.createToolProject(toolProject);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new CommonResponse(e.getMessage()));
        }

        return ResponseEntity.ok(new CommonResponse("Success"));
    }

    @GetMapping("/agent/{id}")
    public ResponseEntity<List<Project>> getAllProjectsByAgent(@PathVariable Long id) {
        User agent = userService.getUserById(id);

        if (agent == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        List<Project> projectList = projectService.getAllProjectsByAgent(id);
        if (projectList == null || projectList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(projectList);
    }

    @PostMapping("/tile/by/task/{id}")
    public ResponseEntity<CommonResponse> createTilesByTaskId(@PathVariable Long id, @RequestBody final List<Tile> tileList) {
        if (tileList == null || tileList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Task task = projectService.getTaskById(id);
        if (task == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CommonResponse("Task not found"));

        projectService.createTilesByTaskId(id, tileList);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResponse("Created"));
    }

    @GetMapping("/tiles/by/task/{id}")
    public ResponseEntity<List<Tile>> getAllTilesByTaskId(@PathVariable Long id) {
        List<Tile> tileList = projectService.getAllTilesByTaskId(id);
        if (tileList == null || tileList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(tileList);
    }

    @GetMapping("/tiles/by/project/{id}")
    public ResponseEntity<List<Tile>> getAllTilesByProjectId(@PathVariable Long id) {
        List<Tile> tileList = projectService.getAllTilesByProjectId(id);
        if (tileList == null || tileList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(tileList);
    }

    @GetMapping("/history/by/plantation/{id}")
    public ResponseEntity<List<ProjectHistory>> getProjectHistoryByPlantationId(@PathVariable Long id) {
        Plantation plantation = organizationService.getPlantationById(id);

        if (plantation == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        long count = projectService.getProjectCountByPlantationId(id);
        if (count == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<ProjectHistory> projectHistoryList = projectService.getProjectHistoryByPlantationId(id);
        if (projectHistoryList == null || projectHistoryList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(projectHistoryList);
    }

    @GetMapping("/total/yield/{id}")
    public ResponseEntity<CommonResponse> getTotalYieldByProjectId(@PathVariable Long id) {
        Project project = projectService.getProjectById(id);
        if (project == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        String totalYield = projectService.getTotalYieldByProjectId(id);
        return ResponseEntity.ok(new CommonResponse(totalYield));
    }

    @GetMapping("/total/stress-pct/{id}")
    public ResponseEntity<CommonResponse> getTotalStressPctByProjectId(@PathVariable Long id) {
        Project project = projectService.getProjectById(id);
        if (project == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        String totalStressPct = projectService.getTotalStressPctByProjectId(id);
        return ResponseEntity.ok(new CommonResponse(totalStressPct));
    }

    @GetMapping("/total/disease-pct/{id}")
    public ResponseEntity<CommonResponse> getTotalDiseasePctByProjectId(@PathVariable Long id) {
        Project project = projectService.getProjectById(id);
        if (project == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        String totalDiseasePct = projectService.getTotalDiseasePctByProjectId(id);
        return ResponseEntity.ok(new CommonResponse(totalDiseasePct));
    }

    private MultipartFile convertTiffToJpg(MultipartFile tifFile) {
        try {
            BufferedImage tifImage = ImageIO.read(tifFile.getInputStream());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(tifImage, "JPG", baos);

            byte[] jpgBytes = baos.toByteArray();

            return new MockMultipartFile(
                    "converted_image_D",
                    "converted_image_D.JPG",
                    "image/jpeg",
                    jpgBytes
            );
        } catch (IOException e) {
            throw new RuntimeException("Error converting .tif to .JPG", e);
        }
    }
}
