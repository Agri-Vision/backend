package com.research.agrivision.business.port.out;

import com.research.agrivision.business.entity.Project;

import java.util.List;

public interface GetProjectPort {
    Project getProjectById(Long id);

    List<Project> getAllProjects();

    List<Project> getAllProjectsByPlantationId(Long id);

    Project getProjectByWebOdmProjectId(String projectId);
}
