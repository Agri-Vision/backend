package com.research.agrivision.business.port.in;

import com.research.agrivision.business.entity.IotReading;
import com.research.hexa.core.UseCase;

import java.util.List;

@UseCase
public interface IotUseCase {
    IotReading createIotReading(IotReading iotReading);

    IotReading getIotReadingById(Long id);

    List<IotReading> getAllIotReadings();
}
