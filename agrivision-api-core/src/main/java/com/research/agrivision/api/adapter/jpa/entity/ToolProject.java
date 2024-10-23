package com.research.agrivision.api.adapter.jpa.entity;

import com.research.agrivision.business.entity.Task;
import com.research.agrivision.business.enums.ToolTaskStatus;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ToolProject extends BaseEntity {
    private int toolProjectId;
    private String toolTaskId;
    private Task task;
    private ToolTaskStatus status;
}
