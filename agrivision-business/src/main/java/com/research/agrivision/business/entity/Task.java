package com.research.agrivision.business.entity;

import com.research.agrivision.business.enums.TaskType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Task extends BaseEntity {
    private Long id;
    private String webOdmTaskId;
    private String mapImage;
    private String mapImageUrl;
    private TaskType taskType;
    private List<Tile> tileList;
    private boolean status;
}
