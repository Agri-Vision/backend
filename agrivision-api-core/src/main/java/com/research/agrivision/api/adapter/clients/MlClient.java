package com.research.agrivision.api.adapter.clients;

import com.research.agrivision.api.adapter.integration.integrate.sample.request.SampleModelRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ml-api-client", url = "${client.model}")
public interface MlClient {

    @PostMapping("/score")
    String getSampleModel(@RequestBody SampleModelRequest sampleModelRequest,
                          @RequestHeader("Authorization") String authorizationHeader);
}
