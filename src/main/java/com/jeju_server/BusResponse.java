package com.jeju_server;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusResponse {
    private List<Bus> buses;
    private List<AllBus> allBuses;
}