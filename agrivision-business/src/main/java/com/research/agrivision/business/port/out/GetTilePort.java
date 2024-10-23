package com.research.agrivision.business.port.out;

import com.research.agrivision.business.entity.Task;
import com.research.agrivision.business.entity.Tile;
import com.research.agrivision.business.enums.TaskType;

import java.util.List;

public interface GetTilePort {
    Tile getTileById(Long tileId);

    List<Tile> getAllTiles();

    List<Tile> getAllTilesByTaskId(Long id);

    List<Tile> getAllTilesByProjectId(Long id);
}
