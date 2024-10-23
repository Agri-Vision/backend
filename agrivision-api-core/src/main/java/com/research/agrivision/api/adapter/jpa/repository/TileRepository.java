package com.research.agrivision.api.adapter.jpa.repository;

import com.research.agrivision.api.adapter.jpa.entity.Tile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TileRepository extends JpaRepository<Tile, Long> {
    List<Tile> findAllByTaskId(Long id);

    List<Tile> findAllByTaskProjectId(Long id);
}
