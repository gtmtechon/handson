package com.handson.gtm.iotmon.controller;

import com.handson.gtm.iotmon.entity.WaterBotState;
import com.handson.gtm.iotmon.repository.WaterBotStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * WaterBotState 엔티티에 대한 RESTful API를 제공하는 컨트롤러.
 * 로봇 상태 정보를 생성, 조회, 업데이트, 삭제합니다.
 */
@RestController // 이 클래스가 REST 컨트롤러임을 선언합니다.
@RequestMapping("/api/waterbots") // 이 컨트롤러의 모든 엔드포인트에 대한 기본 경로를 설정합니다.
public class WaterBotStateController {

    private final WaterBotStateRepository waterBotStateRepository;

    @Autowired // Spring이 WaterBotStateRepository의 인스턴스를 자동으로 주입하도록 합니다.
    public WaterBotStateController(WaterBotStateRepository waterBotStateRepository) {
        this.waterBotStateRepository = waterBotStateRepository;
    }

    /**
     * 모든 WaterBotState 정보를 조회합니다.
     * GET /waterbots
     * @return 모든 로봇 상태 정보 목록
     */
    @GetMapping
    public List<WaterBotState> getAllWaterBotStates() {
        return waterBotStateRepository.findAll();
    }

    /**
     * 특정 botId를 가진 WaterBotState 정보를 조회합니다.
     * GET /waterbots/{botId}
     * @param botId 조회할 로봇의 ID
     * @return 특정 로봇 상태 정보
     */
    @GetMapping("/{botId}")
    public ResponseEntity<WaterBotState> getWaterBotStateById(@PathVariable String botId) {
        Optional<WaterBotState> waterBotState = waterBotStateRepository.findById(botId);
        return waterBotState.map(ResponseEntity::ok) // 존재하면 200 OK와 함께 데이터 반환
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "WaterBot with ID " + botId + " not found")); // 없으면 404 Not Found 에러 반환
    }

    /**
     * 새로운 WaterBotState 정보를 생성하거나 기존 정보를 업데이트합니다.
     * POST /waterbots
     * 주의: 이 엔드포인트는 주로 신규 생성을 위해 사용되며, 기존 ID가 있으면 업데이트됩니다 (UPSERT).
     * 로봇 시뮬레이터와 같이 "최신 상태 업데이트"를 주로 하는 경우 PUT 메서드와 비슷하게 작동할 수 있습니다.
     * @param waterBotState 생성 또는 업데이트할 로봇 상태 정보
     * @return 생성 또는 업데이트된 로봇 상태 정보
     */
    @PostMapping
    public ResponseEntity<WaterBotState> createOrUpdateWaterBotState(@RequestBody WaterBotState waterBotState) {
        // lastUpdated 필드를 현재 시간으로 자동 설정 (로봇 시뮬레이터에서 timestamp를 보내도 여기서 서버 시간으로 덮어씀)
        waterBotState.setLastUpdated(LocalDateTime.now());
        WaterBotState savedState = waterBotStateRepository.save(waterBotState);
        return new ResponseEntity<>(savedState, HttpStatus.CREATED); // 201 Created (신규 생성시) 또는 200 OK (업데이트시)
    }

    /**
     * 특정 botId를 가진 WaterBotState 정보를 업데이트합니다.
     * PUT /waterbots/{botId}
     * 해당 ID의 로봇이 존재하지 않으면 404 Not Found를 반환합니다.
     * @param botId 업데이트할 로봇의 ID
     * @param waterBotStateDetails 업데이트할 로봇 상태 정보 (요청 본문)
     * @return 업데이트된 로봇 상태 정보
     */
    @PutMapping("/{botId}")
    public ResponseEntity<WaterBotState> updateWaterBotState(@PathVariable String botId, @RequestBody WaterBotState waterBotStateDetails) {
        return waterBotStateRepository.findById(botId)
                .map(existingBot -> {
                    // 기존 로봇 정보를 업데이트 (ID는 변경하지 않음)
                    existingBot.setBotName(waterBotStateDetails.getBotName());
                    existingBot.setLocation(waterBotStateDetails.getLocation());
                    existingBot.setLocationCooSys(waterBotStateDetails.getLocationCooSys());
                    existingBot.setStatus(waterBotStateDetails.getStatus());
                    existingBot.setLastUpdated(LocalDateTime.now()); // 업데이트 시간 갱신
                    WaterBotState updatedBot = waterBotStateRepository.save(existingBot);
                    return ResponseEntity.ok(updatedBot); // 200 OK 반환
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "WaterBot with ID " + botId + " not found for update"));
    }

    /**
     * 특정 botId를 가진 WaterBotState 정보를 삭제합니다.
     * DELETE /waterbots/{botId}
     * @param botId 삭제할 로봇의 ID
     * @return 응답 없음 (성공 시 204 No Content)
     */
    @DeleteMapping("/{botId}")
    public ResponseEntity<Void> deleteWaterBotState(@PathVariable String botId) {
        if (waterBotStateRepository.existsById(botId)) {
            waterBotStateRepository.deleteById(botId);
            return ResponseEntity.noContent().build(); // 204 No Content 반환
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "WaterBot with ID " + botId + " not found for deletion");
        }
    }
}