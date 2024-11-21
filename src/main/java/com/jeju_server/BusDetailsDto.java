package com.jeju_server;

import lombok.Data;

import java.security.Timestamp;

@Data
public class BusDetailsDto {
    private String plateNo;
    private Timestamp originalTimestamp;
    private Long aId;
    private Timestamp searchTimestamp;
    private Double localY;
    private Double localX;
    private Long bId;
    private String currStationNm;
    private String routeNum;
    public BusDetailsDto(String plateNo, Timestamp originalTimestamp, Long aId,
                         Timestamp searchTimestamp, Double localY, Double localX,
                         Long bId, String currStationNm, String routeNum) {
        this.plateNo = plateNo;
        this.originalTimestamp = originalTimestamp;
        this.aId = aId;
        this.searchTimestamp = searchTimestamp;
        this.localY = localY;
        this.localX = localX;
        this.bId = bId;
        this.currStationNm = currStationNm;
        this.routeNum = routeNum;
    }
}