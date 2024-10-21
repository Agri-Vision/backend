package com.research.agrivision.api.adapter.jpa.entity;

import com.research.agrivision.business.enums.TaskType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Task extends BaseEntity {
    private String webOdmTaskId;
    private String mapImage;
    private String mapImageUrl;
    private TaskType taskType;
    private String mapImagePng;
    private String mapImagePngUrl;
    private String upperLat;
    private String lowerLat;
    private String upperLng;
    private String lowerLng;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId")
    private Project project;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "taskId")
    private List<Tile> tileList;
}
