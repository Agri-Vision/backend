package com.research.agrivision.business.entity;

import com.research.agrivision.business.enums.ToolTaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ToolProject extends BaseEntity {
    private Long id;
    private int toolProjectId;
    private String toolTaskId;
    private Task task;
    private ToolTaskStatus status;
}
