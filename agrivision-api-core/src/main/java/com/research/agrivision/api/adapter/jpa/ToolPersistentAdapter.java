package com.research.agrivision.api.adapter.jpa;

import com.research.agrivision.api.adapter.clients.ImageToolClient;
import com.research.agrivision.api.adapter.jpa.entity.AvUser;
import com.research.agrivision.api.adapter.jpa.entity.Plantation;
import com.research.agrivision.api.adapter.jpa.repository.ToolProjectRepository;
import com.research.agrivision.business.entity.ToolProject;
import com.research.agrivision.business.entity.imageTool.Request.CreateProjectRequest;
import com.research.agrivision.business.entity.imageTool.Response.CreateProjectResponse;
import com.research.agrivision.business.entity.imageTool.Response.FileUploadResponse;
import com.research.agrivision.business.entity.imageTool.Response.StartProjectResponse;
import com.research.agrivision.business.entity.imageTool.Response.TaskStatusResponse;
import com.research.agrivision.business.enums.ToolTaskStatus;
import com.research.agrivision.business.port.out.ToolPort;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.List;

@Component
public class ToolPersistentAdapter implements ToolPort {
    private final ModelMapper mapper = new ModelMapper();

    private final ToolProjectRepository toolProjectRepository;

    @Autowired
    private ImageToolClient toolClient;

    public ToolPersistentAdapter(ToolProjectRepository toolProjectRepository) {
        this.toolProjectRepository = toolProjectRepository;
    }

    @Override
    public CreateProjectResponse createProject(CreateProjectRequest request) {
        return toolClient.createProject(request);
    }

    @Override
    public FileUploadResponse projectFileUpload(int id, MultipartFile[] files) {
        return toolClient.projectFileUpload(id, files);
    }

    @Override
    public StartProjectResponse startProject(int id) {
        return toolClient.startProject(id);
    }

    @Override
    public TaskStatusResponse getTaskStatus(String taskId) {
        return toolClient.getTaskStatus(taskId);
    }

    @Override
    public void createToolProject(ToolProject toolProject) {
        if (toolProject == null) return;
        com.research.agrivision.api.adapter.jpa.entity.ToolProject dbToolProject = mapper.map(toolProject, com.research.agrivision.api.adapter.jpa.entity.ToolProject.class);
        toolProjectRepository.save(dbToolProject);
    }

    @Override
    public List<ToolProject> getAllToolProjectsByStatus(ToolTaskStatus status) {
        return toolProjectRepository.findAllByStatus(status).stream()
                .sorted(Comparator.comparing(com.research.agrivision.api.adapter.jpa.entity.ToolProject::getId))
                .map(toolProject -> mapper.map(toolProject, com.research.agrivision.business.entity.ToolProject.class))
                .toList();
    }

    @Override
    public void updateToolProjectStatus(Long id, ToolTaskStatus status) {
        com.research.agrivision.api.adapter.jpa.entity.ToolProject toolProject = toolProjectRepository.findById(id).orElse(null);
        if (toolProject == null) return;
        toolProject.setStatus(status);
        toolProjectRepository.save(toolProject);
    }
}
