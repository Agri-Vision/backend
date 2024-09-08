package com.research.agrivision.api.adapter.jpa.repository;

import com.research.agrivision.api.adapter.jpa.entity.AvUserRoleMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvUserRoleMapRepository extends JpaRepository<AvUserRoleMap, Long> {
    AvUserRoleMap findAvUserRoleMapByUserId(Long id);
}
