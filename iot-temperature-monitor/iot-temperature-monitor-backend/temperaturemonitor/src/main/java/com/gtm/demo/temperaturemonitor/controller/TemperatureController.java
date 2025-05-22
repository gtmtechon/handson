package com.gtm.demo.temperaturemonitor.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gtm.demo.temperaturemonitor.service.TemperatureService;
import com.gtm.demo.temperaturemonitor.entity.TemperatureReading;

@RestController
@RequestMapping("/api/temperatures")
public class TemperatureController {
    @Autowired
    private TemperatureService temperatureService;

    // IoT 장비에서 온도 데이터를 전송받는 엔드포인트
    @PostMapping("/reading")
    public ResponseEntity<TemperatureReading> recordTemperature(@RequestBody TemperatureReading reading) {
        return new ResponseEntity<>(temperatureService.saveTemperatureReading(reading), HttpStatus.CREATED);
    }

    @GetMapping("/device/{deviceId}/recent")
    public ResponseEntity<List<TemperatureReading>> getRecentTemperatures(@PathVariable String deviceId, @RequestParam(defaultValue = "100") int limit) {
        return ResponseEntity.ok(temperatureService.getRecentTemperatures(deviceId, limit));
    }

    @GetMapping("/device/{deviceId}/range")
    public ResponseEntity<List<TemperatureReading>> getTemperaturesByRange(@PathVariable String deviceId,
                                                                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                                                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(temperatureService.getTemperaturesByTimeRange(deviceId, start, end));
    }
}