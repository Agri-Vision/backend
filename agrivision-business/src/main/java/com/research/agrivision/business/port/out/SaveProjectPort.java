package com.research.agrivision.business.port.out;

import com.research.agrivision.business.entity.Project;

public interface SaveProjectPort {
    Project createProject(Project project);

    Project updateProject(Project request);
}
