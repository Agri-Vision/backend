package com.research.agrivision.api.adapter.api.controller;

import com.research.agrivision.api.adapter.api.response.CommonResponse;
import com.research.agrivision.business.port.in.FileUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/utility")
public class UtilityController {
    private final FileUseCase fileService;

    @Autowired
    public UtilityController(FileUseCase fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/file/upload")
    public ResponseEntity<CommonResponse> uploadFile(@RequestBody String payload) {
        String fileName = fileService.uploadFile(payload);
        if (fileName == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        return ResponseEntity.ok(new CommonResponse(fileName));
    }

    @GetMapping("/file/{fileName}")
    public ResponseEntity<CommonResponse> getFileUrl(@PathVariable String fileName) {
        String fileUrl = fileService.getSignedUrl(fileName);
        if (fileUrl == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        return ResponseEntity.ok(new CommonResponse(fileUrl));
    }
}
