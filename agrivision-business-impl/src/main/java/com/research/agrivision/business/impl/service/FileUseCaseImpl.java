package com.research.agrivision.business.impl.service;

import com.research.agrivision.business.port.in.FileUseCase;
import com.research.agrivision.business.port.out.FilePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileUseCaseImpl implements FileUseCase {
    @Autowired
    private FilePort filePort;

    @Override
    public String uploadFile(String base64Payload) {
        String fileName = getFileNameFromBase64(base64Payload);

        String base64Data = base64Payload.split(",")[1];
        return filePort.uploadFile(base64Data, fileName);
    }

    @Override
    public String getSignedUrl(String fileName) {
        return filePort.generateSignedUrl(fileName);
    }

    private String getFileNameFromBase64(String base64) {
        String[] parts = base64.split(";base64,");
        String metadata = parts[0];
        String extension = metadata.substring(metadata.lastIndexOf("/") + 1);
        return System.currentTimeMillis() + "." + extension;
    }

}
