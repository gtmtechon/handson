package com.handson.gtm.iotmon.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.handson.gtm.iotmon.entity.TemperatureReading;
import com.handson.gtm.iotmon.service.TemperatureService;

import org.springframework.web.bind.annotation.RequestMapping;




@RestController
@RequestMapping("/api/temperatures")
//@CrossOrigin(origins = {"http://localhost:3000", "https://iotdevicemonitor-h6h8bpepcme4dee2.koreacentral-01.azurewebsites.net"},allowCredentials = "true")
public class TemperatureController {

    private final TemperatureService temperatureService;
    private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);


    // TemperatureService를 주입받음
    public TemperatureController(TemperatureService temperatureService) {
        this.temperatureService = temperatureService;
    }

    @PostMapping("/reading")
    public ResponseEntity<TemperatureReading> addTemperatureReading(@RequestBody TemperatureReading reading) {
        TemperatureReading savedReading = temperatureService.saveTemperatureReading(reading);
        return new ResponseEntity<>(savedReading, HttpStatus.CREATED);
    }

    @GetMapping("/device/{deviceId}/recent")
    public ResponseEntity<List<TemperatureReading>> getRecentTemperaturesByDeviceId(
            @PathVariable String deviceId,
            @RequestParam(defaultValue = "60") int limit) { // limit은 분(minutes) 단위로 받습니다.
        logger.info("Fetching recent temperature readings for deviceId: {} with limit: {}", deviceId, limit);
        List<TemperatureReading> readings = temperatureService.getRecentReadingsByDeviceId(deviceId, limit);
        return ResponseEntity.ok(readings);
    }

    @GetMapping("/device/{deviceId}/all")
    public ResponseEntity<List<TemperatureReading>> getAllTemperaturesByDeviceId(@PathVariable String deviceId) {
        List<TemperatureReading> readings = temperatureService.getAllReadingsByDeviceId(deviceId);
        return ResponseEntity.ok(readings);
    }

    @GetMapping("/{readingId}")
    public ResponseEntity<TemperatureReading> getTemperatureReadingById(@PathVariable Long readingId) {
        return temperatureService.getReadingById(readingId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}