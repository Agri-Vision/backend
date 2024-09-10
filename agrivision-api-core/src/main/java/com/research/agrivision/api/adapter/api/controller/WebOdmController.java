package com.research.agrivision.api.adapter.api.controller;

import com.research.agrivision.api.adapter.api.request.ModelRequest;
import com.research.agrivision.api.adapter.api.response.CommonResponse;
import com.research.agrivision.business.entity.ml.sample.SampleModelRequest;
import com.research.agrivision.business.entity.webodm.AuthenticationRequest;
import com.research.agrivision.business.entity.webodm.AuthenticationResponse;
import com.research.agrivision.business.entity.webodm.ProjectRequest;
import com.research.agrivision.business.port.in.WebOdmUseCase;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@RestController
@RequestMapping("/web-odm")
public class WebOdmController {
    @Value("${web-odm.username}")
    private String username;

    @Value("${web-odm.password}")
    private String password;

    private final WebOdmUseCase webOdmService;

    private final ModelMapper mapper = new ModelMapper();

    public WebOdmController(WebOdmUseCase webOdmUseCase) {
        this.webOdmService = webOdmUseCase;
    }

    @GetMapping("/api/token-auth")
    public ResponseEntity<AuthenticationResponse> getAuthenticationToken() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(username, password);
        AuthenticationResponse response = webOdmService.getAuthenticationToken(authenticationRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/api/projects/{projectId}/tasks/{taskId}/download/orthophoto.tif")
    public ResponseEntity<byte[]> getAuthenticationToken(@PathVariable String projectId,
                                                                 @PathVariable String taskId) {
        return webOdmService.getWebOdmTask(projectId, taskId);
    }

    @PostMapping("/api/projects")
    public ResponseEntity<CommonResponse> createWebOdmProject(@RequestBody ProjectRequest projectRequest) {
        String response = webOdmService.createWebOdmProject(projectRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse(response));
    }

    @PostMapping(value = "/api/projects/{projectId}/tasks", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonResponse> uploadTaskImagesAndOptions(@PathVariable String projectId,
                                      @RequestPart("images") MultipartFile[] images,
                                      @RequestPart("options") String options) {
        String response = webOdmService.uploadTaskImagesAndOptions(projectId, images, options);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse(response));
    }

}
