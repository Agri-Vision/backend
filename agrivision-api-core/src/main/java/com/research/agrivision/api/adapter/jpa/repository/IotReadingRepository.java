package com.research.agrivision.api.adapter.jpa.repository;

import com.research.agrivision.api.adapter.jpa.entity.IotReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IotReadingRepository extends JpaRepository<IotReading, Long> {
    @Query("SELECT i FROM IotReading i " +
            "WHERE (:time = 'daily' AND i.recordedDate = :date) " +
            "   OR (:time = 'weekly' AND i.recordedDate BETWEEN :startOfWeek AND :endOfWeek) " +
            "   OR (:time = 'monthly' AND i.recordedDate BETWEEN :startOfMonth AND :endOfMonth) " +
            "ORDER BY i.id DESC")
    List<IotReading> findByTime(
            @Param("time") String time,
            @Param("date") LocalDate date,
            @Param("startOfWeek") LocalDate startOfWeek,
            @Param("endOfWeek") LocalDate endOfWeek,
            @Param("startOfMonth") LocalDate startOfMonth,
            @Param("endOfMonth") LocalDate endOfMonth
    );

    Optional<IotReading> findFirstByOrderByIdDesc();
}
