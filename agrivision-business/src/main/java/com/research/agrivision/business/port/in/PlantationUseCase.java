package com.research.agrivision.business.port.in;

import com.research.agrivision.business.entity.Plantation;
import com.research.hexa.core.UseCase;

import java.util.List;

@UseCase
public interface PlantationUseCase {
    List<Plantation> getAllPlantations();
}
