package com.handson.gtm.iotmon.service;


import com.handson.gtm.iotmon.entity.WaterBotState;
import com.handson.gtm.iotmon.repository.WaterBotStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * WaterBotState 관련 비즈니스 로직을 처리하는 서비스 클래스.
 * 트랜잭션 관리 및 데이터 유효성 검사와 같은 비즈니스 규칙을 포함할 수 있습니다.
 */
@Service // Spring이 이 클래스를 서비스 빈으로 인식하도록 합니다.
public class WaterBotStateService {

    private final WaterBotStateRepository waterBotStateRepository;

    @Autowired // WaterBotStateRepository 인스턴스를 자동으로 주입받습니다.
    public WaterBotStateService(WaterBotStateRepository waterBotStateRepository) {
        this.waterBotStateRepository = waterBotStateRepository;
    }

    /**
     * 모든 WaterBotState 정보를 조회합니다.
     * @return 모든 로봇 상태 정보 목록
     */
    @Transactional(readOnly = true) // 읽기 전용 트랜잭션 설정으로 성능 최적화
    public List<WaterBotState> findAllBots() {
        return waterBotStateRepository.findAll();
    }

    /**
     * 특정 botId를 가진 WaterBotState 정보를 조회합니다.
     * @param botId 조회할 로봇의 ID
     * @return Optional<WaterBotState> (존재하지 않을 수 있으므로 Optional 사용)
     */
    @Transactional(readOnly = true)
    public Optional<WaterBotState> findBotById(String botId) {
        return waterBotStateRepository.findById(botId);
    }

    /**
     * 새로운 WaterBotState 정보를 생성하거나 기존 정보를 업데이트합니다.
     * 로봇 시뮬레이터와 같이 최신 상태를 지속적으로 업데이트하는 데 사용됩니다.
     * @param waterBotState 저장 또는 업데이트할 로봇 상태 정보
     * @return 저장 또는 업데이트된 로봇 상태 정보
     */
    @Transactional // 쓰기 작업이므로 트랜잭션 설정
    public WaterBotState saveOrUpdateBotState(WaterBotState waterBotState) {
        // 엔티티의 lastUpdated 필드를 서비스 계층에서 현재 시간으로 설정
        waterBotState.setLastUpdated(LocalDateTime.now());
        return waterBotStateRepository.save(waterBotState);
    }

    /**
     * 특정 botId를 가진 WaterBotState 정보를 업데이트합니다.
     * 해당 ID의 로봇이 존재하지 않으면 Optional.empty()를 반환합니다.
     * @param botId 업데이트할 로봇의 ID
     * @param waterBotStateDetails 업데이트할 로봇 상태 정보
     * @return Optional<WaterBotState> (업데이트된 로봇 정보, 존재하지 않을 경우 empty)
     */
    @Transactional
    public Optional<WaterBotState> updateBotState(String botId, WaterBotState waterBotStateDetails) {
        return waterBotStateRepository.findById(botId)
                .map(existingBot -> {
                    existingBot.setBotName(waterBotStateDetails.getBotName());
                    existingBot.setLocation(waterBotStateDetails.getLocation());
                    existingBot.setLocationCooSys(waterBotStateDetails.getLocationCooSys());
                    existingBot.setStatus(waterBotStateDetails.getStatus());
                    existingBot.setLastUpdated(LocalDateTime.now()); // 업데이트 시간 갱신
                    return waterBotStateRepository.save(existingBot);
                });
    }

    /**
     * 특정 botId를 가진 WaterBotState 정보를 삭제합니다.
     * @param botId 삭제할 로봇의 ID
     * @return true (삭제 성공), false (해당 ID의 로봇이 존재하지 않음)
     */
    @Transactional
    public boolean deleteBotById(String botId) {
        if (waterBotStateRepository.existsById(botId)) {
            waterBotStateRepository.deleteById(botId);
            return true;
        }
        return false;
    }
}