package com.research.agrivision.business.impl.service;

import com.research.agrivision.business.entity.IotReading;
import com.research.agrivision.business.port.in.IotUseCase;
import com.research.agrivision.business.port.out.GetIotPort;
import com.research.agrivision.business.port.out.SaveIotPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IotUseCaseImpl implements IotUseCase {
    @Autowired
    private GetIotPort getIotPort;

    @Autowired
    private SaveIotPort saveIotPort;

    @Override
    public IotReading createIotReading(IotReading iotReading) {
        return saveIotPort.createIotReading(iotReading);
    }

    @Override
    public IotReading getIotReadingById(Long id) {
        return getIotPort.getIotReadingById(id);
    }

    @Override
    public List<IotReading> getAllIotReadings() {
        return getIotPort.getAllIotReadings();
    }
}
