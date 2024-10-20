package com.research.agrivision.business.entity.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMaps {
    private MultipartFile rgbMap;
    private MultipartFile rMap;
    private MultipartFile gMap;
    private MultipartFile reMap;
    private MultipartFile nirMap;
}
