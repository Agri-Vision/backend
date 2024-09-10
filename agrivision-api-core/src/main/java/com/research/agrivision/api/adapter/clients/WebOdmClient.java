package com.research.agrivision.api.adapter.clients;

import com.research.agrivision.api.adapter.integration.integrate.webodm.request.AuthenticationRequest;
import com.research.agrivision.api.adapter.integration.integrate.webodm.request.ProjectRequest;
import com.research.agrivision.api.adapter.integration.integrate.webodm.response.AuthenticationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "web-odm-api-client", url = "${web-odm.url}")
public interface WebOdmClient {

    @PostMapping("/api/token-auth/")
    AuthenticationResponse getAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest);

//    @GetMapping("/api/projects/{projectId}/tasks/{taskId}/download/orthophoto.tif")
//    String getWebOdmTask(@PathVariable String projectId,
//                         @PathVariable String taskId,
//                         @RequestHeader("Authorization") String authorizationHeader);

    @GetMapping("/api/projects/{projectId}/tasks/{taskId}/download/orthophoto.tif")
    ResponseEntity<byte[]> getWebOdmTask(@PathVariable String projectId,
                                         @PathVariable String taskId,
                                         @RequestHeader("Authorization") String authorizationHeader);

    @PostMapping("/api/projects/")
    String createWebOdmProject(@RequestBody ProjectRequest projectRequest,
                               @RequestHeader("Authorization") String authorizationHeader);

    @PostMapping(value = "/api/projects/{projectId}/tasks/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String uploadTaskImagesAndOptions(@PathVariable String projectId,
                                      @RequestHeader("Authorization") String authorizationHeader,
                                      @RequestPart("images") MultipartFile[] images,
                                      @RequestPart("options") String options);
}
