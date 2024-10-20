package com.research.agrivision.business.impl.service;

import com.research.agrivision.business.entity.*;
import com.research.agrivision.business.entity.imageTool.ToolReadings;
import com.research.agrivision.business.entity.project.ProjectMaps;
import com.research.agrivision.business.enums.ProjectStatus;
import com.research.agrivision.business.enums.TaskType;
import com.research.agrivision.business.port.in.ProjectUseCase;
import com.research.agrivision.business.port.out.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
public class ProjectUseCaseImpl implements ProjectUseCase {
    @Autowired
    private SaveProjectPort saveProjectPort;

    @Autowired
    private GetProjectPort getProjectPort;

    @Autowired
    private SaveIotPort saveIotPort;

    @Autowired
    private GetIotPort getIotPort;

    @Autowired
    private SaveTaskPort saveTaskPort;

    @Autowired
    private GetTaskPort getTaskPort;

    @Autowired
    private SaveTilePort saveTilePort;

    @Autowired
    private GetTilePort getTilePort;

    @Autowired
    private FilePort filePort;

    @Autowired
    private WebOdmPort webOdmPort;

    @Override
    public Project createProject(Project project) {
        project.setStatus(ProjectStatus.NEW);
        Project dbProject = saveProjectPort.createProject(project);
        if (dbProject == null || dbProject.getTaskList() == null || dbProject.getTaskList().isEmpty()) return dbProject;
        for (Task task : dbProject.getTaskList()) {
            generateTaskSignedUrl(task);
            if (task.getTileList() == null || task.getTileList().isEmpty()) continue;
            for (Tile tile : task.getTileList()) {
                generateTileSignedUrl(tile);
            }
        }
        return dbProject;
    }

    @Override
    public Project getProjectById(Long id) {
        Project project = getProjectPort.getProjectById(id);

        if (project != null && project.getAgent() != null) {
            generateAgentSignedUrl(project.getAgent());
        }
        if (project != null && project.getPlantation() != null) {
            generatePlantationSignedUrl(project.getPlantation());
        }

        if (project == null || project.getTaskList() == null || project.getTaskList().isEmpty()) return project;
        for (Task task : project.getTaskList()) {
            generateTaskSignedUrl(task);
            if (task.getTileList() == null || task.getTileList().isEmpty()) continue;
            for (Tile tile : task.getTileList()) {
                generateTileSignedUrl(tile);
            }
        }
        return project;
    }

    @Override
    public Project updateProject(Project request) {
        request.setStatus(ProjectStatus.PENDING);
        Project project = saveProjectPort.updateProject(request);
        if (project == null || project.getTaskList() == null || project.getTaskList().isEmpty()) return project;
        for (Task task : project.getTaskList()) {
            generateTaskSignedUrl(task);
//            if (project.getWebOdmProjectId() != null && task.getWebOdmTaskId() != null && !task.isStatus()) {
//                webOdmPort.getWebOdmTask(project.getWebOdmProjectId(), task.getWebOdmTaskId());
//            }
            if (task.getTileList() == null || task.getTileList().isEmpty()) continue;
            for (Tile tile : task.getTileList()) {
                generateTileSignedUrl(tile);
            }
        }
        return project;
    }

    @Override
    public List<Project> getAllProjects() {
        List<Project> projectList = getProjectPort.getAllProjects();
        for (Project project : projectList) {

            if (project != null && project.getAgent() != null) {
                generateAgentSignedUrl(project.getAgent());
            }
            if (project != null && project.getPlantation() != null) {
                generatePlantationSignedUrl(project.getPlantation());
            }

            if (project == null || project.getTaskList() == null || project.getTaskList().isEmpty()) continue;
            for (Task task : project.getTaskList()) {
                generateTaskSignedUrl(task);
                if (task.getTileList() == null || task.getTileList().isEmpty()) continue;
                for (Tile tile : task.getTileList()) {
                    generateTileSignedUrl(tile);
                }
            }
        }
        return projectList;
    }

    @Override
    public List<Project> getAllProjectsByPlantationId(Long id) {
        List<Project> projectList = getProjectPort.getAllProjectsByPlantationId(id);
        for (Project project : projectList) {
            if (project != null && project.getAgent() != null) {
                generateAgentSignedUrl(project.getAgent());
            }
            if (project != null && project.getPlantation() != null) {
                generatePlantationSignedUrl(project.getPlantation());
            }
            if (project == null || project.getTaskList() == null || project.getTaskList().isEmpty()) continue;
            for (Task task : project.getTaskList()) {
                generateTaskSignedUrl(task);
                if (task.getTileList() == null || task.getTileList().isEmpty()) continue;
                for (Tile tile : task.getTileList()) {
                    generateTileSignedUrl(tile);
                }
            }
        }
        return projectList;
    }

    @Override
    public Project getProjectByWebOdmProjectId(String projectId) {
        return getProjectPort.getProjectByWebOdmProjectId(projectId);
    }

    @Override
    public Task getTaskByWebOdmTaskId(String taskId) {
        return getTaskPort.getTaskByWebOdmTaskId(taskId);
    }

    @Override
    public void createTile(ToolReadings toolReadings) {
        saveTilePort.createTile(toolReadings);
    }

    @Override
    public List<Project> getAllProjectsByStatus(ProjectStatus status) {
        List<Project> projectList = getProjectPort.getAllProjectsByStatus(status);
        for (Project project : projectList) {
            if (project != null && project.getAgent() != null) {
                generateAgentSignedUrl(project.getAgent());
            }
            if (project != null && project.getPlantation() != null) {
                generatePlantationSignedUrl(project.getPlantation());
            }

            if (project == null || project.getTaskList() == null || project.getTaskList().isEmpty()) continue;
            for (Task task : project.getTaskList()) {
                generateTaskSignedUrl(task);
                if (task.getTileList() == null || task.getTileList().isEmpty()) continue;
                for (Tile tile : task.getTileList()) {
                    generateTileSignedUrl(tile);
                }
            }
        }
        return projectList;
    }

    @Override
    public Tile getTileById(Long tileId) {
        Tile tile = getTilePort.getTileById(tileId);
        if(tile != null)generateTileSignedUrl(tile);
        return tile;
    }

    @Override
    public List<Tile> getAllTiles() {
        return getTilePort.getAllTiles();
    }

    @Override
    public Task getRgbTaskByProjectId(Long id) {
        Task task = getTaskPort.getRgbTaskByProjectId(id);
        if (task != null) {
            generateTaskSignedUrl(task);
            if (task.getTileList() != null && !task.getTileList().isEmpty()) {
                for (Tile tile : task.getTileList()) {
                    generateTileSignedUrl(tile);
                }
            }
        }
        return task;
    }

    @Override
    public Task getTaskById(Long id) {
        return getTaskPort.getTaskById(id);
    }

    @Override
    public void updateTask(Task task) {
        saveTaskPort.updateTask(task);
    }

    @Override
    public void updateProjectMaps(Long id, ProjectMaps projectMaps) {
        if (projectMaps.getRgbMap() != null) {
            Task rgbTask = getTaskPort.getTaskByProjectIdAndType(id, TaskType.RGB);
            if (rgbTask != null) {
                String fileName = uploadTiffFile(projectMaps.getRgbMap());
                rgbTask.setMapImage(fileName);
                //TODO set lat, lng and png image if needed
                saveTaskPort.updateTask(rgbTask);
            }
        }

        if (projectMaps.getRMap() != null) {
            Task rTask = getTaskPort.getTaskByProjectIdAndType(id, TaskType.R);
            if (rTask != null) {
                String fileName = uploadTiffFile(projectMaps.getRMap());
                rTask.setMapImage(fileName);
                saveTaskPort.updateTask(rTask);
            }
        }

        if (projectMaps.getGMap() != null) {
            Task gTask = getTaskPort.getTaskByProjectIdAndType(id, TaskType.G);
            if (gTask != null) {
                String fileName = uploadTiffFile(projectMaps.getGMap());
                gTask.setMapImage(fileName);
                saveTaskPort.updateTask(gTask);
            }
        }

        if (projectMaps.getReMap() != null) {
            Task reTask = getTaskPort.getTaskByProjectIdAndType(id, TaskType.RE);
            if (reTask != null) {
                String fileName = uploadTiffFile(projectMaps.getReMap());
                reTask.setMapImage(fileName);
                saveTaskPort.updateTask(reTask);
            }
        }

        if (projectMaps.getNirMap() != null) {
            Task nirTask = getTaskPort.getTaskByProjectIdAndType(id, TaskType.NIR);
            if (nirTask != null) {
                String fileName = uploadTiffFile(projectMaps.getNirMap());
                nirTask.setMapImage(fileName);
                saveTaskPort.updateTask(nirTask);
            }
        }
    }

    private void generateTaskSignedUrl(Task task) {
        if(task.getMapImage() != null) {
            String imgName = task.getMapImage();
            task.setMapImageUrl(filePort.generateSignedUrl(imgName));
        }
        if(task.getMapImagePng() != null) {
            String pngName = task.getMapImagePng();
            task.setMapImagePngUrl(filePort.generateSignedUrl(pngName));
        }
    }

    private void generateTileSignedUrl(Tile tile) {
        if(tile !=null && tile.getTileImage() != null) {
            String imgName = tile.getTileImage();
            tile.setTileImageUrl(filePort.generateSignedUrl(imgName));
        }
    }

    private void generatePlantationSignedUrl(Plantation plantation) {
        if(plantation !=null && plantation.getPlantationImg() != null) {
            String imgName = plantation.getPlantationImg();
            plantation.setPlantationImgUrl(filePort.generateSignedUrl(imgName));
        }
    }

    private void generateAgentSignedUrl(User agent) {
        if(agent !=null && agent.getProfileImg() != null) {
            String imgName = agent.getProfileImg();
            agent.setProfileImgUrl(filePort.generateSignedUrl(imgName));
        }
    }

    private String uploadTiffFile(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            }

            String fileName = System.currentTimeMillis() + "." + extension;
            byte[] fileBytes = file.getBytes();
            String base64Data = Base64.getEncoder().encodeToString(fileBytes);

            return filePort.uploadFile(base64Data, fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
