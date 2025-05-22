package com.gtm.demo.temperaturemonitor.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "temperature_readings")
@Data // Lombok
@NoArgsConstructor
@AllArgsConstructor
public class TemperatureReading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long readingId;
    private String deviceId;
    private double temperature;
    private LocalDateTime recordedAt;
}