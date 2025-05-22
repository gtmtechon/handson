package com.gtm.demo.temperaturemonitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gtm.demo.temperaturemonitor.entity.Device;

public interface DeviceRepository extends JpaRepository<Device, String> {
}

