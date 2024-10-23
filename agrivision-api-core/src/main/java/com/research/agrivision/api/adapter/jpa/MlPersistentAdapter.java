package com.research.agrivision.api.adapter.jpa;

import com.research.agrivision.api.adapter.clients.DiseaseClient;
import com.research.agrivision.api.adapter.clients.MlClient;
import com.research.agrivision.api.adapter.clients.StressClient;
import com.research.agrivision.api.adapter.clients.YieldClient;
import com.research.agrivision.api.adapter.integration.integrate.sample.response.ModelResponse;
import com.research.agrivision.api.adapter.integration.integrate.sample.response.StressResponse;
import com.research.agrivision.business.entity.ml.sample.DiseaseRequest;
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

    @Autowired
    private DiseaseClient diseaseClient;

    @Autowired
    private StressClient stressClient;

    @Autowired
    private YieldClient yieldClient;

    @Override
    public String getSampleModel(SampleModelRequest request, String authToken) {
        com.research.agrivision.api.adapter.integration.integrate.sample.request.SampleModelRequest sampleModelRequest =
                mapper.map(request, com.research.agrivision.api.adapter.integration.integrate.sample.request.SampleModelRequest.class);
        return mlClient.getSampleModel(sampleModelRequest, authToken);
    }

    @Override
    public String getDiseaseModel(DiseaseRequest diseaseRequest) {
        com.research.agrivision.api.adapter.integration.integrate.sample.request.DiseaseRequest disRequest =
                mapper.map(diseaseRequest, com.research.agrivision.api.adapter.integration.integrate.sample.request.DiseaseRequest.class);
        ModelResponse modelResponse = diseaseClient.getDiseaseModel(disRequest);
        String result = modelResponse.getPrediction().get(0).toString();
        if ("1".equalsIgnoreCase(result)) {
            return "yes";
        } else {
            return "no";
        }
    }

    @Override
    public String getStressModel(DiseaseRequest diseaseRequest) {
        com.research.agrivision.api.adapter.integration.integrate.sample.request.DiseaseRequest disRequest =
                mapper.map(diseaseRequest, com.research.agrivision.api.adapter.integration.integrate.sample.request.DiseaseRequest.class);
        StressResponse stressResponse = stressClient.getStressModel(disRequest);
        String prediction = stressResponse.getPrediction();
        if ("Stressed".equalsIgnoreCase(prediction)) {
            return "yes";
        } else {
            return "no";
        }
//        rendvi = 0.127
//        temperature = 30.7571428571429
//        humidity = 76.51428
//        uv_level = 0.7442
//        soil_moisture = 271.5714
//        pressure = 95037.1428571429
//        altitude = 551.428571428571
    }

    @Override
    public String getYieldModel(DiseaseRequest diseaseRequest) {
        com.research.agrivision.api.adapter.integration.integrate.sample.request.DiseaseRequest disRequest =
                mapper.map(diseaseRequest, com.research.agrivision.api.adapter.integration.integrate.sample.request.DiseaseRequest.class);
        ModelResponse modelResponse = yieldClient.getYieldModel(disRequest);
        return modelResponse.getPrediction().get(0).toString();
    }
}
