package com.research.agrivision.business.port.in;

import com.research.hexa.core.UseCase;
import org.springframework.web.multipart.MultipartFile;

@UseCase
public interface FileUseCase {
    /**
     * Upload file to the storage
     * @param base64Payload - blob content as base64 string
     * @return - name of the file
     */
    String uploadFile(String base64Payload);

    /**
     * Get file public url
     * @param fileName - uploaded file name
     * @return - signed public url of the file
     */
    String getSignedUrl(String fileName);

    String uploadTiffFile(MultipartFile file);
}
