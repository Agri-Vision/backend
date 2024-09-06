package com.research.agrivision.business.port.out;

import com.research.agrivision.business.entity.webodm.AuthenticationRequest;
import com.research.agrivision.business.entity.webodm.AuthenticationResponse;

public interface WebOdmPort {
    AuthenticationResponse getAuthenticationToken(AuthenticationRequest authenticationRequest);

    String getWebOdmTask(String projectId, String taskId, String authorizationHeader);
}
