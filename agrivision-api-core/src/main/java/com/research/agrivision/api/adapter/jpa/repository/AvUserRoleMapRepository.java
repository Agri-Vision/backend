package com.research.agrivision.api.adapter.jpa.repository;

import com.research.agrivision.api.adapter.jpa.entity.AvUserRoleMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvUserRoleMapRepository extends JpaRepository<AvUserRoleMap, Long> {
    AvUserRoleMap findAvUserRoleMapByUserId(Long id);
    List<AvUserRoleMap> findAvUserRoleMapsByRoleId(Long id);
    List<AvUserRoleMap> findAvUserRoleMapsByRoleRoleNameIgnoreCase(String roleName);
}
