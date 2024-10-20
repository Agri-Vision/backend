package com.research.agrivision.business.port.out;

import com.research.agrivision.business.entity.Plantation;

import java.util.List;

public interface GetPlantationPort {
    Plantation getPlantationById(Long plantationId);

    List<Plantation> getAllPlantations();
}
