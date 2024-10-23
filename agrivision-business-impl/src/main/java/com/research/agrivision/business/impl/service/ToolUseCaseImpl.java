package com.research.agrivision.business.impl.service;

import com.research.agrivision.business.entity.ToolProject;
import com.research.agrivision.business.entity.imageTool.Request.CreateProjectRequest;
import com.research.agrivision.business.entity.imageTool.Response.CreateProjectResponse;
import com.research.agrivision.business.entity.imageTool.Response.FileUploadResponse;
import com.research.agrivision.business.entity.imageTool.Response.StartProjectResponse;
import com.research.agrivision.business.entity.imageTool.Response.TaskStatusResponse;
import com.research.agrivision.business.enums.ToolTaskStatus;
import com.research.agrivision.business.port.in.ToolUseCase;
import com.research.agrivision.business.port.out.ToolPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ToolUseCaseImpl implements ToolUseCase {

    @Autowired
    private ToolPort toolPort;

    @Override
    public CreateProjectResponse createProject(CreateProjectRequest request) {
        return toolPort.createProject(request);
    }

    @Override
    public FileUploadResponse projectFileUpload(int id, MultipartFile[] files) {
        return toolPort.projectFileUpload(id, files);
    }

    @Override
    public StartProjectResponse startProject(int id) {
        return toolPort.startProject(id);
    }

    @Override
    public TaskStatusResponse getTaskStatus(String taskId) {
        return toolPort.getTaskStatus(taskId);
    }

    @Override
    public void createToolProject(ToolProject toolProject) {
        toolPort.createToolProject(toolProject);
    }

    @Override
    public List<ToolProject> getAllToolProjectsByStatus(ToolTaskStatus status) {
        return toolPort.getAllToolProjectsByStatus(status);
    }

    @Override
    public void updateToolProjectStatus(Long id, ToolTaskStatus status) {
        toolPort.updateToolProjectStatus(id, status);
    }
}
