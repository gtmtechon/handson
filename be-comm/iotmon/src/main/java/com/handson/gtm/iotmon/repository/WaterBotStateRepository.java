package com.handson.gtm.iotmon.repository;

import com.handson.gtm.iotmon.entity.WaterBotState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * WaterBotState 엔티티에 대한 데이터베이스 CRUD 작업을 처리하는 JPA Repository.
 * Spring Data JPA가 자동으로 구현을 생성합니다.
 */
@Repository // Spring이 이 인터페이스를 Repository 빈으로 인식하도록 합니다.
public interface WaterBotStateRepository extends JpaRepository<WaterBotState, String> {
    // JpaRepository를 상속받음으로써 WaterBotState 엔티티(첫 번째 제네릭 파라미터)와
    // 해당 엔티티의 ID 타입(botId는 String, 두 번째 제네릭 파라미터)을 기반으로
    // findAll(), findById(), save(), delete() 등 기본적인 CRUD 메서드를 자동으로 제공받습니다.
    // 필요에 따라 여기에 사용자 정의 쿼리 메서드를 추가할 수 있습니다.
}