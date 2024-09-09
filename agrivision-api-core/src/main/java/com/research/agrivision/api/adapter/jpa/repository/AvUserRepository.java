package com.research.agrivision.api.adapter.jpa.repository;

import com.research.agrivision.api.adapter.jpa.entity.AvUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AvUserRepository extends JpaRepository<AvUser, Long> {
    Optional<AvUser> findAvUserByEmail(String email);
}
