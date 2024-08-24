package com.research.agrivision.business.port.out;

import com.research.agrivision.business.entity.ml.sample.SampleModelRequest;

public interface MlPort {
    String getSampleModel(SampleModelRequest request, String authToken);
}
