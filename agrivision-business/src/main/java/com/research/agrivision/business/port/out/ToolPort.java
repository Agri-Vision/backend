package com.research.agrivision.business.port.out;

import com.research.agrivision.business.entity.ToolProject;
import com.research.agrivision.business.entity.imageTool.Request.CreateProjectRequest;
import com.research.agrivision.business.entity.imageTool.Response.CreateProjectResponse;
import com.research.agrivision.business.entity.imageTool.Response.FileUploadResponse;
import com.research.agrivision.business.entity.imageTool.Response.StartProjectResponse;
import com.research.agrivision.business.entity.imageTool.Response.TaskStatusResponse;
import com.research.agrivision.business.enums.ToolTaskStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ToolPort {
    CreateProjectResponse createProject(CreateProjectRequest request);

    FileUploadResponse projectFileUpload(int id, MultipartFile[] files);

    StartProjectResponse startProject(int id);

    TaskStatusResponse getTaskStatus(String taskId);

    void createToolProject(ToolProject toolProject);

    List<ToolProject> getAllToolProjectsByStatus(ToolTaskStatus status);

    void updateToolProjectStatus(Long id, ToolTaskStatus status);
}
