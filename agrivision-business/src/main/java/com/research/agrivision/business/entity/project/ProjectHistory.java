package com.research.agrivision.business.entity.project;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.research.agrivision.business.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectHistory {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime projectCompletedDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime projectCreatedDate;

    private Long projectId;
    private String projectName;
    private User agent;
    private BigDecimal totalYield;
    private BigDecimal stressPct;
    private BigDecimal diseasePct;
}
