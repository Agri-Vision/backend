package com.research.agrivision.api.adapter.jpa.repository;

import com.research.agrivision.api.adapter.jpa.entity.ToolProject;
import com.research.agrivision.business.enums.ToolTaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToolProjectRepository  extends JpaRepository<ToolProject, Long> {
    List<ToolProject> findAllByStatus(ToolTaskStatus status);
}
