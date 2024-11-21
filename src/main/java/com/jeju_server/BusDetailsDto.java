package com.jeju_server;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusDetailsDto {
    private String plateNo;           // 차량 번호
    private String Original;   // 가장 최신 타임스탬프
    private Long A_id;            // 가장 큰 ID
    private String Filter; // 검색 테이블의 가장 최신 타임스탬프
    private Double local_Y;            // Y 좌표
    private Double local_X;            // X 좌표
    private Long B_id;                  // ID
    private String currStationName;   // 현재 정류장 이름
    private String routeNum;          // 노선 번호
}