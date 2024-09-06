package com.research.agrivision.business.impl.service;

import com.research.agrivision.business.entity.webodm.AuthenticationRequest;
import com.research.agrivision.business.entity.webodm.AuthenticationResponse;
import com.research.agrivision.business.port.in.WebOdmUseCase;
import com.research.agrivision.business.port.out.WebOdmPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebOdmUseCaseImpl implements WebOdmUseCase {

    @Autowired
    WebOdmPort webOdmPort;

    @Override
    public AuthenticationResponse getAuthenticationToken(AuthenticationRequest authenticationRequest) {
        return webOdmPort.getAuthenticationToken(authenticationRequest);
    }

    @Override
    public String getWebOdmTask(String projectId, String taskId, String authorizationHeader) {
        return webOdmPort.getWebOdmTask(projectId, taskId, authorizationHeader);
    }
}
