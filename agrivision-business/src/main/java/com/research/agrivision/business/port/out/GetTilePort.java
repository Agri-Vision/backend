package com.research.agrivision.business.port.out;

import com.research.agrivision.business.entity.Tile;

import java.util.List;

public interface GetTilePort {
    Tile getTileById(Long tileId);

    List<Tile> getAllTiles();
}
