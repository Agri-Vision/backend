package com.research.agrivision.api.adapter.jpa.repository;

import com.research.agrivision.api.adapter.jpa.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
