package com.research.agrivision.business.port.out;

import com.research.agrivision.business.entity.Task;

public interface GetTaskPort {
    Task getTaskByWebOdmTaskId(String taskId);

    Task getTaskById(Long id);
}
