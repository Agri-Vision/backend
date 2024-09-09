package com.research.agrivision.business.port.in;

import com.research.agrivision.business.entity.ml.sample.DiseaseRequest;
import com.research.agrivision.business.entity.ml.sample.SampleModelRequest;
import com.research.hexa.core.UseCase;

@UseCase
public interface MlUseCase {
    String getSampleModel(SampleModelRequest request, String authToken);

    String getDiseaseModel(DiseaseRequest diseaseRequest);

    String getStressModel(DiseaseRequest diseaseRequest);
}
