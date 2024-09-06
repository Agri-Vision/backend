package com.research.agrivision.api.adapter.api.controller;

import com.research.agrivision.api.adapter.api.request.ModelRequest;
import com.research.agrivision.business.entity.ml.sample.SampleModelRequest;
import com.research.agrivision.business.entity.webodm.AuthenticationRequest;
import com.research.agrivision.business.entity.webodm.AuthenticationResponse;
import com.research.agrivision.business.port.in.WebOdmUseCase;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> getAuthenticationToken(@PathVariable String projectId,
                                                         @PathVariable String taskId,
                                                         @RequestHeader("Authorization") String authorizationHeader) {
        String response = webOdmService.getWebOdmTask(projectId, taskId, authorizationHeader);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
