package com.research.agrivision.api.adapter.integration.integrate.tool.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadResponse {
    public String filename;
    public int image_id;
    public String status;
}
