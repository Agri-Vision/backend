package com.research.agrivision.api.adapter.jpa.repository;

import com.research.agrivision.api.adapter.jpa.entity.IotDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IotDeviceRepository extends JpaRepository<IotDevice, Long> {
}
