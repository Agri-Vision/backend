package com.research.agrivision.api.adapter.clients;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.sas.BlobContainerSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import com.azure.storage.blob.specialized.BlockBlobClient;
import com.azure.storage.common.sas.SasProtocol;
import com.research.agrivision.business.port.out.FilePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

@Component
public class FileClient implements FilePort {

    @Autowired
    private BlobContainerClient blobContainerClient;

    @Override
    public String uploadFile(String base64Data, String fileName) {
        byte[] fileData = Base64.getDecoder().decode(base64Data);

        BlockBlobClient blobClient = blobContainerClient.getBlobClient(fileName).getBlockBlobClient();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileData);
        blobClient.upload(inputStream, fileData.length);

        return fileName;
    }

    @Override
    public String generateSignedUrl(String fileName) {
        BlobContainerSasPermission blobContainerSasPermission = new BlobContainerSasPermission().setReadPermission(true);
        BlobServiceSasSignatureValues builder = new BlobServiceSasSignatureValues(OffsetDateTime.now()
                .plus(10, ChronoUnit.YEARS),
                blobContainerSasPermission).setProtocol(SasProtocol.HTTPS_ONLY);

        if (fileName.contains(".pdf")) {
            builder.setContentType("application/pdf");
//        } else if (fileName.contains(".png")) {
//            builder.setContentType("image/png");
//        } else if (fileName.contains(".jpg") || fileName.contains(".jpeg")) {
//            builder.setContentType("image/jpeg");
//        } else if (fileName.contains(".tif") || fileName.contains(".tiff")) {
//            builder.setContentType("image/tiff");
        } else if (!fileName.contains(".xlsx") && !fileName.contains(".csv")) {
            builder.setContentType("text/plain");
        }

        BlobClient client = blobContainerClient.getBlobClient(fileName);

        return client.exists() ? String.format("https://%s.blob.core.windows.net/%s/%s?%s", client.getAccountName(),
                client.getContainerName(), fileName, client.generateSas(builder)) : null;
    }
}
