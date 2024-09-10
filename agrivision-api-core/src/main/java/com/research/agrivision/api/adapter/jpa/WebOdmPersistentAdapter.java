package com.research.agrivision.api.adapter.jpa;

import com.research.agrivision.api.adapter.clients.WebOdmClient;
import com.research.agrivision.api.adapter.jpa.entity.Project;
import com.research.agrivision.api.adapter.jpa.entity.Task;
import com.research.agrivision.api.adapter.jpa.repository.ProjectRepository;
import com.research.agrivision.api.adapter.jpa.repository.TaskRepository;
import com.research.agrivision.business.entity.webodm.AuthenticationRequest;
import com.research.agrivision.business.entity.webodm.AuthenticationResponse;
import com.research.agrivision.business.entity.webodm.ProjectRequest;
import com.research.agrivision.business.port.out.FilePort;
import com.research.agrivision.business.port.out.WebOdmPort;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

@Component
public class WebOdmPersistentAdapter implements WebOdmPort {
    @Value("${web-odm.username}")
    private String username;

    @Value("${web-odm.password}")
    private String password;

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    private final ModelMapper mapper = new ModelMapper();

    @Autowired
    private WebOdmClient webOdmClient;

    @Autowired
    private FilePort filePort;

    public WebOdmPersistentAdapter(TaskRepository taskRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public AuthenticationResponse getAuthenticationToken(AuthenticationRequest authenticationRequest) {
        com.research.agrivision.api.adapter.integration.integrate.webodm.request.AuthenticationRequest authRequest =
                mapper.map(authenticationRequest, com.research.agrivision.api.adapter.integration.integrate.webodm.request.AuthenticationRequest.class);
        com.research.agrivision.api.adapter.integration.integrate.webodm.response.AuthenticationResponse authenticationResponse = webOdmClient.getAuthenticationToken(authRequest);
        return mapper.map(authenticationResponse, AuthenticationResponse.class);
    }

    @Override
    public ResponseEntity<byte[]> getWebOdmTask(String projectId, String taskId) {
//        String authorizationHeader = generateWebOdmAuthToken();
//        return webOdmClient.getWebOdmTask(projectId, taskId, authorizationHeader);
        String authorizationHeader = generateWebOdmAuthToken();
        ResponseEntity<byte[]> response = webOdmClient.getWebOdmTask(projectId, taskId, authorizationHeader);

        if (response.getStatusCode() == HttpStatus.OK) {
            byte[] fileData = response.getBody();

            // Save file data to database
            if (fileData != null) {
                saveFileToDatabase(projectId, taskId, fileData);
            }
        } else {
            throw new RuntimeException("Failed to retrieve file from WebODM");
        }
        return response;
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


    private void saveFileToDatabase(String projectId, String taskId, byte[] fileData) {
        String base64data = Base64.getEncoder().encodeToString(fileData);
        String originalFileName = getFileNameFromBase64(base64data);
        Task task = taskRepository.findByWebOdmTaskId(taskId).orElse(null);
        Project project = projectRepository.findByWebOdmProjectId(projectId).orElse(null);
        if (task != null) {
            String taskType = task.getTaskType().toString();
            String imageName = taskType.equalsIgnoreCase("RGB") ? taskType + ".jpg" : taskType + ".tif";
            String contentType = taskType.equalsIgnoreCase("RGB") ? "image/jpeg" : "image/tiff";
            String fileName = filePort.uploadFile(base64data, imageName);
            task.setMapImage(fileName);
            task.setProject(project);
            task.setStatus(true);
            taskRepository.save(task);
            // TODO send task map to image tool
//            MultipartFile multipartFile = new MockMultipartFile(imageName, originalFileName, contentType, fileData);
        }
    }

    private String getFileNameFromBase64(String base64) {
        String[] parts = base64.split(";base64,");
        String metadata = parts[0];
        String extension = metadata.substring(metadata.lastIndexOf("/") + 1);
        return System.currentTimeMillis() + "." + extension;
    }
}
