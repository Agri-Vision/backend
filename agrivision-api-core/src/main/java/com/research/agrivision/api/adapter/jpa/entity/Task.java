package com.research.agrivision.api.adapter.jpa.entity;

import com.research.agrivision.business.enums.TaskType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Task extends BaseEntity{
    private String webOdmTaskId;
    private String mapImage;
    private TaskType taskType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId")
    private Project project;
}
