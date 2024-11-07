package com.jeju_server;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bus_test_topic3")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ROUTE_NUM", nullable = false, columnDefinition = "TEXT")
    private String routeNum; // 노선 번호

    @Column(name = "LOCAL_Y", nullable = false)
    private Double localY; // 위도

    @Column(name = "LOCAL_X", nullable = false)
    private Double localX; // 경도

    @Column(name = "CURR_STATION_NM", nullable = false, columnDefinition = "TEXT")
    private String currStationNm; // 현재 정류장 이름

    @Column(name = "PLATE_NO", nullable = false, columnDefinition = "TEXT")
    private String plateNo; // 차량 번호

    @Column(name = "TIMESTAMP", nullable = false, columnDefinition = "TEXT")
    private String timestamp; // 타임스탬프

}