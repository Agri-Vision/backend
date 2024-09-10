package com.research.agrivision.business.port.in;

import com.research.agrivision.business.entity.Plantation;
import com.research.agrivision.business.entity.Tile;

public interface YieldUseCase {
    String validateTile(Tile tile, Plantation plantation);
}
