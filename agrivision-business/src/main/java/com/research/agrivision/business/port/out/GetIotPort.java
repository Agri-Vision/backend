package com.research.agrivision.business.port.out;

import com.research.agrivision.business.entity.IotReading;

import java.util.List;

public interface GetIotPort {
    IotReading getIotReadingById(Long id);

    List<IotReading> getAllIotReadings();

    List<IotReading> getIotReadingByTimePeriod(String time);

    String getEnvironmentData();

    IotReading getLatestReading();
}
