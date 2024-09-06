package com.research.agrivision.api.adapter.clients;

import com.research.agrivision.api.adapter.integration.integrate.webodm.request.AuthenticationRequest;
import com.research.agrivision.api.adapter.integration.integrate.webodm.response.AuthenticationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "web-odm-api-client", url = "${web-odm.url}")
public interface WebOdmClient {

    @PostMapping("/api/token-auth/")
    AuthenticationResponse getAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest);

    @GetMapping("/api/projects/{projectId}/tasks/{taskId}/download/orthophoto.tif")
    String getWebOdmTask(@PathVariable String projectId,
                         @PathVariable String taskId,
                         @RequestHeader("Authorization") String authorizationHeader);
}
