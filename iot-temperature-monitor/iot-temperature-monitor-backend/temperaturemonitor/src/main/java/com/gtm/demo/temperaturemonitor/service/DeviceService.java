package com.gtm.demo.temperaturemonitor.service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtm.demo.temperaturemonitor.entity.Device;
import com.gtm.demo.temperaturemonitor.repository.DeviceRepository;

@Service
public class DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;

    public Device registerDevice(Device device) {
        device.setRegisteredAt(LocalDateTime.now());
        return deviceRepository.save(device);
    }

    // Removed duplicate updateDevice method to resolve compilation error.

    public void deleteDevice(String deviceId) {
        deviceRepository.deleteById(deviceId);
    }

    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    public Optional<Device> updateDevice(String deviceId, Device device) {
        return deviceRepository.findById(deviceId)
                .map(existingDevice -> {
                    existingDevice.setDeviceName(device.getDeviceName());
                    existingDevice.setLocation(device.getLocation());
                    return deviceRepository.save(existingDevice);
                });
    }

    public Optional<Device> getDeviceById(String deviceId) {
        return deviceRepository.findById(deviceId);
    }

}