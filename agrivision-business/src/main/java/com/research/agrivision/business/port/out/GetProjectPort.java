package com.research.agrivision.business.port.out;

import com.research.agrivision.business.entity.Project;
import com.research.agrivision.business.enums.ProjectStatus;

import java.util.List;

public interface GetProjectPort {
    Project getProjectById(Long id);

    List<Project> getAllProjects();

    List<Project> getAllProjectsByPlantationId(Long id);

    Project getProjectByWebOdmProjectId(String projectId);

    List<Project> getAllProjectsByStatus(ProjectStatus status);

    List<Project> getAllProjectsByAgent(Long id);
}
