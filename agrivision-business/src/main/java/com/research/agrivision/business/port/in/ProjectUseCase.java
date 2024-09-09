package com.research.agrivision.business.port.in;

import com.research.agrivision.business.entity.Project;
import com.research.hexa.core.UseCase;

import java.util.List;

@UseCase
public interface ProjectUseCase {
    Project createProject(Project project);

    Project getProjectById(Long id);

    Project updateProject(Project request);

    List<Project> getAllProjects();

    List<Project> getAllProjectsByPlantationId(Long id);
}
