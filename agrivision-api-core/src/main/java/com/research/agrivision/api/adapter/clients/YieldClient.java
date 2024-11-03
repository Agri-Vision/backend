package com.research.agrivision.api.adapter.clients;

import com.research.agrivision.api.adapter.integration.integrate.sample.request.DiseaseRequest;
import com.research.agrivision.api.adapter.integration.integrate.sample.response.ModelResponse;
import com.research.agrivision.api.adapter.integration.integrate.sample.response.YieldResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "yield-api-client", url = "${client.yield}")
public interface YieldClient {
    @PostMapping("/predict")
    YieldResponse getYieldModel(@RequestBody DiseaseRequest diseaseRequest);
}
