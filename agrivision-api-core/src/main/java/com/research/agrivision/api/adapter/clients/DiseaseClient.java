package com.research.agrivision.api.adapter.clients;

import com.research.agrivision.api.adapter.integration.integrate.sample.request.DiseaseRequest;
import com.research.agrivision.api.adapter.integration.integrate.sample.response.ModelResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "disease-api-client", url = "${client.disease}")
public interface DiseaseClient {

    @PostMapping("/predict")
    ModelResponse getDiseaseModel(@RequestBody DiseaseRequest diseaseRequest);
}
