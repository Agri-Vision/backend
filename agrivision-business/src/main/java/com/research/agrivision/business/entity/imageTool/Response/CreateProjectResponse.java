package com.research.agrivision.business.entity.imageTool.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProjectResponse {
    public int code;
    public String message;
    public int project_id;
}
