package com.research.agrivision.business.port.out;

import com.research.agrivision.business.entity.webodm.AuthenticationRequest;
import com.research.agrivision.business.entity.webodm.AuthenticationResponse;
import com.research.agrivision.business.entity.webodm.ProjectRequest;
import org.springframework.web.multipart.MultipartFile;

public interface WebOdmPort {
    AuthenticationResponse getAuthenticationToken(AuthenticationRequest authenticationRequest);

    String getWebOdmTask(String projectId, String taskId);

    String createWebOdmProject(ProjectRequest projectRequest);

    String uploadTaskImagesAndOptions(String projectId, MultipartFile[] images, String options);
}
