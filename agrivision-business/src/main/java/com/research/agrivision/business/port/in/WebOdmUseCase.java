package com.research.agrivision.business.port.in;

import com.research.agrivision.business.entity.webodm.AuthenticationRequest;
import com.research.agrivision.business.entity.webodm.AuthenticationResponse;
import com.research.agrivision.business.entity.webodm.ProjectRequest;
import com.research.hexa.core.UseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@UseCase
public interface WebOdmUseCase {
    AuthenticationResponse getAuthenticationToken(AuthenticationRequest authenticationRequest);

    ResponseEntity<byte[]> getWebOdmTask(String projectId, String taskId);

    String createWebOdmProject(ProjectRequest projectRequest);

    String uploadTaskImagesAndOptions(String projectId, MultipartFile[] images, String options);
}
