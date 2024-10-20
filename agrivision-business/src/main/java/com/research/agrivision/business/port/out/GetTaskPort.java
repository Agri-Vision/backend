package com.research.agrivision.business.port.out;

import com.research.agrivision.business.entity.Task;
import com.research.agrivision.business.enums.TaskType;

public interface GetTaskPort {
    Task getTaskByWebOdmTaskId(String taskId);

    Task getTaskById(Long id);

    Task getRgbTaskByProjectId(Long id);

    Task getTaskByProjectIdAndType(Long id, TaskType type);
}
