package com.handson.gtm.iotmon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.handson.gtm.iotmon.entity.Device;

public interface DeviceRepository extends JpaRepository<Device, String> {
}

