package com.research.agrivision.business.impl.service;

import com.research.agrivision.business.port.in.FileUseCase;
import com.research.agrivision.business.port.out.FilePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

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

    @Override
    public String uploadTiffFile(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            }

            String fileName = System.currentTimeMillis() + "." + extension;
            byte[] fileBytes = file.getBytes();
            String base64Data = Base64.getEncoder().encodeToString(fileBytes);

            return filePort.uploadFile(base64Data, fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getFileNameFromBase64(String base64) {
        String[] parts = base64.split(";base64,");
        String metadata = parts[0];
        String extension = metadata.substring(metadata.lastIndexOf("/") + 1);
        return System.currentTimeMillis() + "." + extension;
    }

}
