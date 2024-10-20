package com.research.agrivision.api.adapter.api.request;

import com.research.hexa.core.Request;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
public class ProjectMapRequest implements Request {
    private MultipartFile rgbMap;
    private MultipartFile rMap;
    private MultipartFile gMap;
    private MultipartFile reMap;
    private MultipartFile nirMap;
}
