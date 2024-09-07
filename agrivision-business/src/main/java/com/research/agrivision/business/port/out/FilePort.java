package com.research.agrivision.business.port.out;

public interface FilePort {
    /**
     * Upload files to the storage
     * @param payload - blob content as base64
     * @param fileName - name of the uploading file
     * @return - uploaded file name
     */
    String uploadFile(String payload, String fileName);

    /**
     * Generate signed url for file
     * @param fileName - name of the uploaded file
     * @return - signed url of the file
     */
    String generateSignedUrl(String fileName);
}
