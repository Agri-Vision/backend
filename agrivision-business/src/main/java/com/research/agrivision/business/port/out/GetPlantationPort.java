package com.research.agrivision.business.port.out;

import com.research.agrivision.business.entity.Plantation;

public interface GetPlantationPort {
    Plantation getPlantationById(Long plantationId);
}
