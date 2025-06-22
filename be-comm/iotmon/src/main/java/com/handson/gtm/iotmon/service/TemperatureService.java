package com.handson.gtm.iotmon.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.handson.gtm.iotmon.entity.TemperatureReading;
import com.handson.gtm.iotmon.repository.TemperatureReadingRepository;




@Service
public class TemperatureService {
    private final TemperatureReadingRepository temperatureReadingRepository;

    public TemperatureService(TemperatureReadingRepository temperatureReadingRepository) {
        this.temperatureReadingRepository = temperatureReadingRepository;
    }

   /**
     * 새로운 온도 데이터를 저장합니다.
     * @param reading 저장할 TemperatureReading 객체
     * @return 저장된 TemperatureReading 객체
     */
    public TemperatureReading saveTemperatureReading(TemperatureReading reading) {
        // recordedAt이 설정되지 않았다면 현재 시간으로 설정
        if (reading.getRecordedAt() == null) {
            reading.setRecordedAt(LocalDateTime.now());
        }
        return temperatureReadingRepository.save(reading);
    }

    /**
     * 특정 deviceId에 대한 모든 온도 데이터를 조회합니다.
     * @param deviceId 장비 ID
     * @return 해당 장비의 모든 온도 데이터 목록
     */
    public List<TemperatureReading> getAllReadingsByDeviceId(String deviceId) {
        return temperatureReadingRepository.findByDeviceIdOrderByRecordedAtDesc(deviceId);
    }

    /**
     * 특정 deviceId에 대해 최근 N분 이내의 온도 데이터를 조회합니다.
     * (프론트엔드에서 limit=60이면 60분 이내 데이터)
     * @param deviceId 장비 ID
     * @param minutes 조회할 최근 분(minute) 수
     * @return 해당 장비의 최근 N분 이내 온도 데이터 목록
     */
    public List<TemperatureReading> getRecentReadingsByDeviceId(String deviceId, int minutes) {
        // 현재 시간으로부터 minutes 전의 시간을 계산
        LocalDateTime startTime = LocalDateTime.now().minusMinutes(minutes);
        // Repository에서 해당 시간 이후의 데이터를 조회
        return temperatureReadingRepository.findRecentByDeviceIdAndRecordedAtAfterOrderByRecordedAtDesc(deviceId, startTime);
    }

    /**
     * 특정 readingId로 온도 데이터를 조회합니다.
     * @param readingId 온도 데이터 ID
     * @return Optional<TemperatureReading>
     */
    public Optional<TemperatureReading> getReadingById(Long readingId) {
        return temperatureReadingRepository.findById(readingId);
    }

    /**
     * 모든 온도 데이터를 삭제합니다. (주의: 개발용)
     */
    public void deleteAllReadings() {
        temperatureReadingRepository.deleteAll();
    }
}