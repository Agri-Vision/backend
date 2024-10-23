package com.research.agrivision.business.port.out;

import com.research.agrivision.business.entity.Tile;
import com.research.agrivision.business.entity.imageTool.ToolReadings;

import java.util.List;

public interface SaveTilePort {
    void createTile(ToolReadings toolReadings);

    void createTilesByTaskId(Long id, List<Tile> tileList);
}
