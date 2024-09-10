package com.research.agrivision.business.entity.imageTool;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ToolReadings {
    private String projectId;
    private String taskId;
    private Double ndvi;
    private Double rendvi;
    private Double cire;
    private Double pri;
}
