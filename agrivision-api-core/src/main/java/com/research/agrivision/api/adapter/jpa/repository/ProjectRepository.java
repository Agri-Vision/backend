package com.research.agrivision.api.adapter.jpa.repository;

import com.research.agrivision.api.adapter.jpa.entity.Project;
import com.research.agrivision.business.enums.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findAllByPlantationId(Long id);

    Optional<Project> findByWebOdmProjectId(String projectId);

    List<Project> findAllByStatus(ProjectStatus status);

    List<Project> findAllByAgentId(Long id);
}
