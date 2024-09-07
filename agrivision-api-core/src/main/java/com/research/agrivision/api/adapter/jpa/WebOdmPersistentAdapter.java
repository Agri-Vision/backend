package com.research.agrivision.api.adapter.jpa;

import com.research.agrivision.api.adapter.clients.WebOdmClient;
import com.research.agrivision.business.entity.webodm.AuthenticationRequest;
import com.research.agrivision.business.entity.webodm.AuthenticationResponse;
import com.research.agrivision.business.entity.webodm.ProjectRequest;
import com.research.agrivision.business.port.out.FilePort;
import com.research.agrivision.business.port.out.WebOdmPort;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

@Component
public class WebOdmPersistentAdapter implements WebOdmPort {
    @Value("${web-odm.username}")
    private String username;

    @Value("${web-odm.password}")
    private String password;

    private final ModelMapper mapper = new ModelMapper();

    @Autowired
    private WebOdmClient webOdmClient;

    @Autowired
    private FilePort filePort;

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
//        String authorizationHeader = generateWebOdmAuthToken();
//        byte[] imageBytes = webOdmClient.getWebOdmTask(projectId, taskId, authorizationHeader).getBytes();
//        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
//        String fileName = generateUniqueFileName();
//        String uploadedFileName = filePort.uploadFile(base64Image, fileName);
//        return filePort.generateSignedUrl(uploadedFileName);
    }

    @Override
    public String createWebOdmProject(ProjectRequest projectRequest) {
        com.research.agrivision.api.adapter.integration.integrate.webodm.request.ProjectRequest proRequest =
                mapper.map(projectRequest, com.research.agrivision.api.adapter.integration.integrate.webodm.request.ProjectRequest.class);
        String authorizationHeader = generateWebOdmAuthToken();
        return webOdmClient.createWebOdmProject(proRequest, authorizationHeader);
    }

    @Override
    public String uploadTaskImagesAndOptions(String projectId, MultipartFile[] images, String options) {
        String authorizationHeader = generateWebOdmAuthToken();
        return webOdmClient.uploadTaskImagesAndOptions(projectId, authorizationHeader, images, options);
    }

    private String generateWebOdmAuthToken() {
        com.research.agrivision.api.adapter.integration.integrate.webodm.request.AuthenticationRequest authenticationRequest =
                new com.research.agrivision.api.adapter.integration.integrate.webodm.request.AuthenticationRequest();
        authenticationRequest.setUsername(username);
        authenticationRequest.setPassword(password);
        com.research.agrivision.api.adapter.integration.integrate.webodm.response.AuthenticationResponse authenticationResponse = webOdmClient.getAuthenticationToken(authenticationRequest);
        return authenticationResponse.getToken();
    }

    private String generateUniqueFileName() {
        return "image_" + System.currentTimeMillis() + ".png";
    }
}
