package com.research.agrivision.api.adapter.jpa.entity;

import com.research.agrivision.business.enums.ToolTaskStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ToolProject extends BaseEntity {
    private int toolProjectId;
    private String toolTaskId;
    private ToolTaskStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "taskId")
    private Task task;
}
