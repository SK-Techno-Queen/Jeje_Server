package com.jeju_server;

import lombok.Data;
@Data
public class BusDetailsDto {
    private String plateNo;
    private String originalTimestamp;
    private Long aId;
    private String searchTimestamp;
    private Double localY;
    private Double localX;
    private String currStationNm;
    private String routeNum;
    public BusDetailsDto(String plateNo, String originalTimestamp, Long aId,
                         String searchTimestamp, Double localY, Double localX,
                         String currStationNm, String routeNum) {
        this.plateNo = plateNo;
        this.originalTimestamp = originalTimestamp;
        this.aId = aId;
        this.searchTimestamp = searchTimestamp;
        this.localY = localY;
        this.localX = localX;
        this.currStationNm = currStationNm;
        this.routeNum = routeNum;
    }
}