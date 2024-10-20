package com.research.agrivision.business.impl.service;

import com.research.agrivision.business.entity.ml.sample.DiseaseRequest;
import com.research.agrivision.business.entity.ml.sample.SampleModelRequest;
import com.research.agrivision.business.port.in.MlUseCase;
import com.research.agrivision.business.port.out.MlPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MlUseCaseImpl implements MlUseCase {
    @Autowired
    private MlPort mlPort;

    @Override
    public String getSampleModel(SampleModelRequest request, String authToken) {
        return mlPort.getSampleModel(request, authToken);
    }

    @Override
    public String getDiseaseModel(DiseaseRequest diseaseRequest) {
        return mlPort.getDiseaseModel(diseaseRequest);
    }

    @Override
    public String getStressModel(DiseaseRequest diseaseRequest) {
        return mlPort.getStressModel(diseaseRequest);
    }

    @Override
    public String getYieldModel(DiseaseRequest diseaseRequest) {
        return mlPort.getYieldModel(diseaseRequest);
    }
}
