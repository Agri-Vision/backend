package com.research.agrivision.api.adapter.jpa;

import com.research.agrivision.api.adapter.clients.DiseaseClient;
import com.research.agrivision.api.adapter.clients.MlClient;
import com.research.agrivision.api.adapter.clients.StressClient;
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
        if (result.equalsIgnoreCase("1")) {
            return "Brown Blight Detected";
        } else {
            return "Not Diseased";
        }
    }

    @Override
    public String getStressModel(DiseaseRequest diseaseRequest) {
        com.research.agrivision.api.adapter.integration.integrate.sample.request.DiseaseRequest disRequest =
                mapper.map(diseaseRequest, com.research.agrivision.api.adapter.integration.integrate.sample.request.DiseaseRequest.class);
        StressResponse stressResponse = stressClient.getStressModel(disRequest);
        return stressResponse.getPrediction();
//        rendvi = 0.127
//        temperature = 30.7571428571429
//        humidity = 76.51428
//        uv_level = 0.7442
//        soil_moisture = 271.5714
//        pressure = 95037.1428571429
//        altitude = 551.428571428571
    }
}
