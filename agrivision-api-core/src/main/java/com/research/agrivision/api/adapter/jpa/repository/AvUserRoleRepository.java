package com.research.agrivision.api.adapter.jpa.repository;

import com.research.agrivision.api.adapter.jpa.entity.AvUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvUserRoleRepository extends JpaRepository<AvUserRole, Long> {
    List<AvUserRole> findAllAvUserRolesByRoleType(String roleType);
}
