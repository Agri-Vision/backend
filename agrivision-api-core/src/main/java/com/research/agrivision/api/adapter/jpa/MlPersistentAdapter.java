package com.research.agrivision.api.adapter.jpa;

import com.research.agrivision.api.adapter.clients.MlClient;
import com.research.agrivision.business.entity.ml.sample.SampleModelRequest;
import com.research.agrivision.business.port.out.MlPort;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MlPersistentAdapter implements MlPort {
    private final ModelMapper mapper = new ModelMapper();

    @Autowired
    private MlClient mlClient;

    @Override
    public String getSampleModel(SampleModelRequest request, String authToken) {
        com.research.agrivision.api.adapter.integration.integrate.sample.request.SampleModelRequest sampleModelRequest =
                mapper.map(request, com.research.agrivision.api.adapter.integration.integrate.sample.request.SampleModelRequest.class);
        return mlClient.getSampleModel(sampleModelRequest, authToken);
    }
}
