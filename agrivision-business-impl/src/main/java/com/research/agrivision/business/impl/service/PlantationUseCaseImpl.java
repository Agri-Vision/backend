package com.research.agrivision.business.impl.service;

import com.research.agrivision.business.entity.Plantation;
import com.research.agrivision.business.port.in.PlantationUseCase;
import com.research.agrivision.business.port.out.FilePort;
import com.research.agrivision.business.port.out.GetPlantationPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlantationUseCaseImpl implements PlantationUseCase {
    @Autowired
    private GetPlantationPort getPlantationPort;

    @Autowired
    private FilePort filePort;

    @Override
    public List<Plantation> getAllPlantations() {
        return getPlantationPort.getAllPlantations();
    }

    private void generatePlantationSignedUrl(Plantation plantation) {
        if(plantation.getPlantationImg() != null) {
            String imgName = plantation.getPlantationImg();
            plantation.setPlantationImgUrl(filePort.generateSignedUrl(imgName));
        }
    }
}
