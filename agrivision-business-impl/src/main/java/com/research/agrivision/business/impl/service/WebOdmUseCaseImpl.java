package com.research.agrivision.business.impl.service;

import com.research.agrivision.business.entity.webodm.AuthenticationRequest;
import com.research.agrivision.business.entity.webodm.AuthenticationResponse;
import com.research.agrivision.business.entity.webodm.ProjectRequest;
import com.research.agrivision.business.port.in.WebOdmUseCase;
import com.research.agrivision.business.port.out.WebOdmPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class WebOdmUseCaseImpl implements WebOdmUseCase {

    @Autowired
    WebOdmPort webOdmPort;

    @Override
    public AuthenticationResponse getAuthenticationToken(AuthenticationRequest authenticationRequest) {
        return webOdmPort.getAuthenticationToken(authenticationRequest);
    }

    @Override
    public String getWebOdmTask(String projectId, String taskId) {
        return webOdmPort.getWebOdmTask(projectId, taskId);
    }

    @Override
    public String createWebOdmProject(ProjectRequest projectRequest) {
        return webOdmPort.createWebOdmProject(projectRequest);
    }

    @Override
    public String uploadTaskImagesAndOptions(String projectId, MultipartFile[] images, String options) {
        return webOdmPort.uploadTaskImagesAndOptions(projectId, images, options);
    }
}
