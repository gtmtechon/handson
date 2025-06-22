package com.handson.gtm.iotmon.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.handson.gtm.iotmon.entity.TemperatureReading;



// TemperatureReadingRepository.java
public interface TemperatureReadingRepository extends JpaRepository<TemperatureReading, Long> {
    List<TemperatureReading> findByDeviceIdOrderByRecordedAtDesc(String deviceId, Pageable pageable);
    List<TemperatureReading> findByDeviceIdAndRecordedAtBetweenOrderByRecordedAtAsc(String deviceId, LocalDateTime start, LocalDateTime end);

        // 특정 deviceId에 대한 모든 온도 데이터를 recordedAt 기준으로 내림차순 정렬하여 조회
    List<TemperatureReading> findByDeviceIdOrderByRecordedAtDesc(String deviceId);

    // 특정 deviceId에 대해 지정된 시간 이후의 온도 데이터를 recordedAt 기준으로 내림차순 정렬하여 조회
    // 이것이 프론트엔드의 'recent' 조회에 사용됩니다.
    @Query("SELECT tr FROM TemperatureReading tr WHERE tr.deviceId = :deviceId AND tr.recordedAt >= :startTime ORDER BY tr.recordedAt DESC")
    List<TemperatureReading> findRecentByDeviceIdAndRecordedAtAfterOrderByRecordedAtDesc(
            @Param("deviceId") String deviceId,
            @Param("startTime") LocalDateTime startTime
    );

    // deviceId와 recordedAt을 기준으로 최근 N개의 데이터를 가져오는 커스텀 쿼리
    // LIMIT 기능은 JPA/Hibernate에서 직접 지원하지 않으므로, Service 레이어에서 Pageable을 사용하거나
    // Native Query 또는 Criteria API를 사용해야 할 수 있습니다.
    // 여기서는 Service에서 상위 N개를 제한하도록 구현하겠습니다.
    
}