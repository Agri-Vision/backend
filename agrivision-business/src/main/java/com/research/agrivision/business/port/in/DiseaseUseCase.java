package com.research.agrivision.business.port.in;

import com.research.agrivision.business.entity.Tile;
import com.research.hexa.core.UseCase;

@UseCase
public interface DiseaseUseCase {
    String getDiseaseScoreByTileId(Tile tile);
}
