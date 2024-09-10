package com.research.agrivision.business.port.out;

import com.research.agrivision.business.entity.Tile;

public interface GetTilePort {
    Tile getTileById(Long tileId);
}
