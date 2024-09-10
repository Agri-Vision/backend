package com.research.agrivision.api.adapter.clients;

import com.research.agrivision.api.adapter.integration.integrate.sample.request.DiseaseRequest;
import com.research.agrivision.api.adapter.integration.integrate.sample.response.ModelResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "yield-api-client", url = "${client.yield}")
public interface YieldClient {
    @PostMapping("/predict")
    ModelResponse getYieldModel(@RequestBody DiseaseRequest diseaseRequest);
}
