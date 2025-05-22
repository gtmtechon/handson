package com.gtm.demo.temperaturemonitor.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.gtm.demo.temperaturemonitor.entity.TemperatureReading;


// TemperatureReadingRepository.java
public interface TemperatureReadingRepository extends JpaRepository<TemperatureReading, Long> {
    List<TemperatureReading> findByDeviceIdOrderByRecordedAtDesc(String deviceId, Pageable pageable);
    List<TemperatureReading> findByDeviceIdAndRecordedAtBetweenOrderByRecordedAtAsc(String deviceId, LocalDateTime start, LocalDateTime end);
}