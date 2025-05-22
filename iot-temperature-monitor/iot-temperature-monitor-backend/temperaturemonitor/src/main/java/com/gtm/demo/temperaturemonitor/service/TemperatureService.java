package com.gtm.demo.temperaturemonitor.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.gtm.demo.temperaturemonitor.entity.TemperatureReading;
import com.gtm.demo.temperaturemonitor.repository.TemperatureReadingRepository;


@Service
public class TemperatureService {
    @Autowired
    private TemperatureReadingRepository temperatureReadingRepository;


    public TemperatureReading saveTemperatureReading(TemperatureReading reading) {
        reading.setRecordedAt(LocalDateTime.now());
        TemperatureReading savedReading = temperatureReadingRepository.save(reading);
        return savedReading;
    }

    public List<TemperatureReading> getRecentTemperatures(String deviceId, int limit) {
        return temperatureReadingRepository.findByDeviceIdOrderByRecordedAtDesc(deviceId, PageRequest.of(0, limit));
    }

    public List<TemperatureReading> getTemperaturesByTimeRange(String deviceId, LocalDateTime start, LocalDateTime end) {
        return temperatureReadingRepository.findByDeviceIdAndRecordedAtBetweenOrderByRecordedAtAsc(deviceId, start, end);
    }
}