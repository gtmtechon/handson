
package com.handson.gtm.iotmon.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "waterbot_status")
@Data // Lombok: Getter, Setter, toString, equals, hashCode 자동 생성
@NoArgsConstructor // Lombok: 기본 생성자 자동 생성
@AllArgsConstructor // Lombok: 모든 필드를 인자로 받는 생성자 자동 생성
public class WaterBotState {
    @Id @Column(name = "botid")
    private String botId;
    @Column(name = "botname")
    private String botName;
    private String location;
    @Column(name = "locationcoosys")
    private String locationCooSys;
    private String status;
     @Column(name = "lastupdated")
    private LocalDateTime lastUpdated; // 등록 시간
}