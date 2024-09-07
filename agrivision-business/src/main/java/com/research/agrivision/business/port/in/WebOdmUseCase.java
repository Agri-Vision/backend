package com.research.agrivision.business.port.in;

import com.research.agrivision.business.entity.webodm.AuthenticationRequest;
import com.research.agrivision.business.entity.webodm.AuthenticationResponse;
import com.research.hexa.core.UseCase;

@UseCase
public interface WebOdmUseCase {
    AuthenticationResponse getAuthenticationToken(AuthenticationRequest authenticationRequest);

    String getWebOdmTask(String projectId, String taskId);
}
