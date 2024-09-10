package com.research.agrivision.business.port.in;

import com.research.agrivision.business.entity.Tile;

public interface StressUseCase {
    String getStressByTileId(Tile tile);
}
