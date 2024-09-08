package com.research.agrivision.api.adapter.jpa;

import com.research.agrivision.business.port.out.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProjectPersistentAdapter implements GetProjectPort, GetTaskPort, GetTilePort, SaveProjectPort, SaveTaskPort, SaveTilePort {
    private ModelMapper mapper = new ModelMapper();
}
