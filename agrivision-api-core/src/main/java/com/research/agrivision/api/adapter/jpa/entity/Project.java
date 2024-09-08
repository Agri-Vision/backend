package com.research.agrivision.api.adapter.jpa.entity;

import com.research.agrivision.business.enums.ProjectStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Project extends BaseEntity {
    private String webOdmProjectId;
    private String projectName;
    private ProjectStatus status;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "projectId")
    private List<IotDevice> iotDeviceList;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "projectId")
    private List<Task> taskList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id")
    private AvUser agent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plantationId")
    private Plantation plantation;
}