package com.research.agrivision.api.adapter.jpa;

import com.research.agrivision.api.adapter.clients.WebOdmClient;
import com.research.agrivision.business.entity.webodm.AuthenticationRequest;
import com.research.agrivision.business.entity.webodm.AuthenticationResponse;
import com.research.agrivision.business.port.out.WebOdmPort;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WebOdmPersistentAdapter implements WebOdmPort {
    @Value("${web-odm.username}")
    private String username;

    @Value("${web-odm.password}")
    private String password;

    private final ModelMapper mapper = new ModelMapper();

    @Autowired
    private WebOdmClient webOdmClient;

    @Override
    public AuthenticationResponse getAuthenticationToken(AuthenticationRequest authenticationRequest) {
        com.research.agrivision.api.adapter.integration.integrate.webodm.request.AuthenticationRequest authRequest =
                mapper.map(authenticationRequest, com.research.agrivision.api.adapter.integration.integrate.webodm.request.AuthenticationRequest.class);
        com.research.agrivision.api.adapter.integration.integrate.webodm.response.AuthenticationResponse authenticationResponse = webOdmClient.getAuthenticationToken(authRequest);
        return mapper.map(authenticationResponse, AuthenticationResponse.class);
    }

    @Override
    public String getWebOdmTask(String projectId, String taskId) {
        String authorizationHeader = generateWebOdmAuthToken();
        return webOdmClient.getWebOdmTask(projectId, taskId, authorizationHeader);
    }

    private String generateWebOdmAuthToken() {
        com.research.agrivision.api.adapter.integration.integrate.webodm.request.AuthenticationRequest authenticationRequest =
                new com.research.agrivision.api.adapter.integration.integrate.webodm.request.AuthenticationRequest();
        authenticationRequest.setUsername(username);
        authenticationRequest.setPassword(password);
        com.research.agrivision.api.adapter.integration.integrate.webodm.response.AuthenticationResponse authenticationResponse = webOdmClient.getAuthenticationToken(authenticationRequest);
        return authenticationResponse.getToken();
    }
}