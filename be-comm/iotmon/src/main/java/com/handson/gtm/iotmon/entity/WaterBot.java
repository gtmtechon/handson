package com.handson.gtm.iotmon.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "WaterBot")
@Data // Lombok: Getter, Setter, toString, equals, hashCode 자동 생성
@NoArgsConstructor // Lombok: 기본 생성자 자동 생성
@AllArgsConstructor // Lombok: 모든 필드를 인자로 받는 생성자 자동 생성
public class WaterBot {
    @Id    
    private String botId;
    private String botName;
    private String productionDate;
    private String owner;
    private String botType;
}