package com.research.agrivision.business.entity;

import com.research.agrivision.business.enums.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Project extends BaseEntity {
    private Long id;
    private String webOdmProjectId;
    private String projectName;
    private ProjectStatus status;
    private User agent;
    private List<IotDevice> iotDeviceList;
    private List<Task> taskList;
}
