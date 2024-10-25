package com.research.agrivision.business.port.in;

import com.research.agrivision.business.entity.Project;
import com.research.agrivision.business.entity.Task;
import com.research.agrivision.business.entity.Tile;
import com.research.agrivision.business.entity.imageTool.ToolReadings;
import com.research.agrivision.business.entity.project.ProjectHistory;
import com.research.agrivision.business.enums.ProjectStatus;
import com.research.hexa.core.UseCase;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@UseCase
public interface ProjectUseCase {
    Project createProject(Project project);

    Project getProjectById(Long id);

    Project updateProject(Project request);

    List<Project> getAllProjects();

    List<Project> getAllProjectsByPlantationId(Long id);

    Project getProjectByWebOdmProjectId(String projectId);

    Task getTaskByWebOdmTaskId(String taskId);

    void createTile(ToolReadings request);

    List<Project> getAllProjectsByStatus(ProjectStatus status);

    Tile getTileById(Long tileId);

    List<Tile> getAllTiles();

    Task getRgbTaskByProjectId(Long id);

    Task getTaskById(Long id);

    void updateTask(Task task);

    void updateProjectMaps(Long id, MultipartFile rgbMap);

    List<Project> getAllProjectsByAgent(Long id);

    void createTilesByTaskId(Long id, List<Tile> tileList);

    List<Tile> getAllTilesByTaskId(Long id);

    List<Tile> getAllTilesByProjectId(Long id);

    List<ProjectHistory> getProjectHistoryByPlantationId(Long id);

    long getProjectCountByPlantationId(Long id);
}
