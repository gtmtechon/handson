package com.gtm.demo.temperaturemonitor.controller;


import org.slf4j.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

// Add missing imports for Device and DeviceService
import com.gtm.demo.temperaturemonitor.entity.Device;
import com.gtm.demo.temperaturemonitor.service.DeviceService;

@RestController
@RequestMapping("/api/devices") // 이 컨트롤러의 기본 경로
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true") // 프론트엔드 포트에 맞게 수정해야 함
public class DeviceController {
    private final DeviceService deviceService;
    private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);

    // DeviceService를 주입받는 생성자 (의존성 주입)
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }


    /**
     * 모든 등록된 장비 목록을 조회합니다.
     * GET /api/devices
     * @return 장비 목록
     */
    @GetMapping
    public ResponseEntity<List<Device>> getAllDevices() {
        List<Device> devices = deviceService.getAllDevices();
        return ResponseEntity.ok(devices);
    }

    /**
     * 특정 deviceId를 가진 장비를 조회합니다.
     * GET /api/devices/{deviceId}
     * @param deviceId 조회할 장비의 ID
     * @return 장비 정보 (찾지 못하면 404 Not Found)
     */
    @GetMapping("/{deviceId}")
    public ResponseEntity<Device> getDeviceById(@PathVariable String deviceId) {
        return deviceService.getDeviceById(deviceId)
                .map(ResponseEntity::ok) // 장비를 찾으면 200 OK와 함께 반환
                .orElseGet(() -> ResponseEntity.notFound().build()); // 찾지 못하면 404 Not Found 반환
    }

    /**
     * 새로운 장비를 등록합니다.
     * POST /api/devices
     * @param device 등록할 장비 정보 (JSON 형태의 요청 본문)
     * @return 등록된 장비 정보 (이미 존재하는 ID면 409 Conflict)
     */
    @PostMapping
    public ResponseEntity<Device> registerDevice(@RequestBody Device device) {
        try{
        // 등록 시간을 서버에서 자동 설정 (프론트엔드에서 보내지 않아도 됨)
        device.setRegisteredAt(LocalDateTime.now());
        if(device.getDeviceId()==null||device.getDeviceId().isEmpty())
            device.setDeviceId(java.util.UUID.randomUUID().toString().replaceAll("-", ""));
        if(device.getDeviceName()==null||device.getDeviceName().isEmpty()) throw new IllegalArgumentException("Device name cannot be null or empty");
            //파일로그 추가
            logger.info("Device registered: " + device);
             Device newDevice = deviceService.registerDevice(device);

            return new ResponseEntity<>(newDevice, HttpStatus.CREATED); // 201 Created
           
        } catch (IllegalArgumentException e) {
            // deviceId가 이미 존재할 경우 예외 처리
            logger.error("error: ", e.toString());
            return new ResponseEntity<>(null, HttpStatus.CONFLICT); // 409 Conflict
        }
    }

    /**
     * 기존 장비 정보를 수정합니다.
     * PUT /api/devices/{deviceId}
     * @param deviceId 수정할 장비의 ID
     * @param deviceDetails 업데이트할 장비 정보
     * @return 수정된 장비 정보 (찾지 못하면 404 Not Found)
     */
    @PutMapping("/{deviceId}")
    public ResponseEntity<Device> updateDevice(@PathVariable String deviceId, @RequestBody Device deviceDetails) {
        logger.info("update : " + deviceId);

        return deviceService.updateDevice(deviceId, deviceDetails)
                .map(ResponseEntity::ok) // 수정 성공 시 200 OK
                .orElseGet(() -> ResponseEntity.notFound().build()); // 찾지 못하면 404 Not Found
    }

    /**
     * 특정 deviceId를 가진 장비를 삭제합니다.
     * DELETE /api/devices/{deviceId}
     * @param deviceId 삭제할 장비의 ID
     * @return 삭제 성공 시 204 No Content
     */
    @DeleteMapping("/{deviceId}")
    public ResponseEntity<Void> deleteDevice(@PathVariable String deviceId) {
        deviceService.deleteDevice(deviceId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    /** selectAll
     * 장비 목록을 조회합니다.
     * @return 장비 목록
     * @throws Exception 예외 발생 시
     *  
     */
    @GetMapping("/selectAll")
    public ResponseEntity<List<Device>> selectAll() throws Exception {
        List<Device> devices = deviceService.getAllDevices();
        return new ResponseEntity<>(devices, HttpStatus.OK);
    }
    /**
     * 장비를 등록합니다.
     * @param device 장비 정보
     * @return 등록된 장비 정보
     * @throws Exception 예외 발생 시
     */
    @PostMapping("/insert")
    public ResponseEntity<Device> insert(@RequestBody Device device) throws Exception {
        logger.info("Device registered: " + device);

        device.setRegisteredAt(LocalDateTime.now());
        Device newDevice = deviceService.registerDevice(device);
        return new ResponseEntity<>(newDevice, HttpStatus.CREATED);
    }


    /**
     * 장비를 삭제합니다.
     * @param deviceId 삭제할 장비 ID
     * @return 삭제 성공 여부
     * @throws Exception 예외 발생 시
     */
    @DeleteMapping("/delete/{deviceId}")
    public ResponseEntity<Void> delete(@PathVariable String deviceId) throws Exception {
        deviceService.deleteDevice(deviceId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}