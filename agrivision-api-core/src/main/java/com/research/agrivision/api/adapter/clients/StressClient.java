package com.research.agrivision.api.adapter.clients;

import com.research.agrivision.api.adapter.integration.integrate.sample.request.DiseaseRequest;
import com.research.agrivision.api.adapter.integration.integrate.sample.response.StressResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "stress-api-client", url = "${client.stress}")
public interface StressClient {
    @PostMapping("/predict")
    StressResponse getStressModel(@RequestBody DiseaseRequest diseaseRequest);
}
