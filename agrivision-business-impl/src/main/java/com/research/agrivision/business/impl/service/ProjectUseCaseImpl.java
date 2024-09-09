package com.research.agrivision.business.impl.service;

import com.research.agrivision.business.entity.Project;
import com.research.agrivision.business.entity.Task;
import com.research.agrivision.business.entity.Tile;
import com.research.agrivision.business.enums.ProjectStatus;
import com.research.agrivision.business.port.in.ProjectUseCase;
import com.research.agrivision.business.port.out.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public Project createProject(Project project) {
        project.setStatus(ProjectStatus.CREATED);
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
        request.setStatus(ProjectStatus.PROCESSING);
        Project project = saveProjectPort.updateProject(request);
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
    public List<Project> getAllProjects() {
        List<Project> projectList = getProjectPort.getAllProjects();
        for (Project project : projectList) {
            if (project == null || project.getTaskList() == null || project.getTaskList().isEmpty()) return projectList;
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
            if (project == null || project.getTaskList() == null || project.getTaskList().isEmpty()) return projectList;
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

    private void generateTaskSignedUrl(Task task) {
        if(task.getMapImage() != null) {
            String imgName = task.getMapImage();
            task.setMapImageUrl(filePort.generateSignedUrl(imgName));
        }
    }

    private void generateTileSignedUrl(Tile tile) {
        if(tile.getTileImage() != null) {
            String imgName = tile.getTileImage();
            tile.setTileImageUrl(filePort.generateSignedUrl(imgName));
        }
    }
}
