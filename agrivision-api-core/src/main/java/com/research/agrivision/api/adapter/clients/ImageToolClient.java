package com.research.agrivision.api.adapter.clients;

import com.research.agrivision.business.entity.imageTool.Request.CreateProjectRequest;
import com.research.agrivision.business.entity.imageTool.Response.CreateProjectResponse;
import com.research.agrivision.business.entity.imageTool.Response.StartProjectResponse;
import com.research.agrivision.business.entity.imageTool.Response.TaskStatusResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "image-tool-api-client", url = "${client.tool}")
public interface ImageToolClient {
    @PostMapping("/project/new")
    CreateProjectResponse createProject(@RequestBody CreateProjectRequest request);

    @PostMapping(value = "/projects/{id}/upload", consumes = "multipart/form-data")
    void projectFileUpload(@PathVariable int id, @RequestPart("files[]") MultipartFile[] files);

    @PostMapping("/project/{id}/start")
    StartProjectResponse startProject(@PathVariable int id);

    @GetMapping("/task-status/{taskId}")
    TaskStatusResponse getTaskStatus(@PathVariable String taskId);
}
