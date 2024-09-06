package com.research.agrivision.business.port.out;

import com.research.agrivision.business.entity.IotReading;

public interface SaveIotPort {
    IotReading createIotReading(IotReading iotReading);

    void deleteAllIotReadings();
}
