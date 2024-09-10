package com.research.agrivision.business.port.out;

import com.research.agrivision.business.entity.imageTool.ToolReadings;

public interface SaveTilePort {
    void createTile(ToolReadings toolReadings);
}
