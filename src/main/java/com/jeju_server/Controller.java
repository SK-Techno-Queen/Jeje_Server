package com.jeju_server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class Controller {

    private final RestTemplate restTemplate;
    private final BusRepository busRepository;

    @Value("${visitjeju.api.key}")
    private String apiKey;
    private final AllBusRepository allBusRepository;

    @GetMapping("/spot")
    public ResponseEntity<String> spot() {
        String url = String.format("http://api.visitjeju.net/vsjApi/contents/searchList?apiKey=%s&locale=kr&category=c1&page=1", apiKey);

        // Visit Jeju API에 요청을 보냅니다.
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        return ResponseEntity.ok(response.getBody());
    }
    @GetMapping("/bus")
    public ResponseEntity<?> bus() {
//        List<Bus> buses = busRepository.findTop50ByOrderByTimestampDesc();
//        List<AllBus> allBuses = allBusRepository.findTop50ByOrderByTimestampDesc();
        List<?> response = busRepository.findBusDetails();

//        BusResponse response = new BusResponse(buses, allBuses);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/allbus")
    public ResponseEntity<List<AllBus>> gatAll() {
        List<AllBus> buses = allBusRepository.findTop50ByOrderByTimestampDesc();

        return ResponseEntity.ok(buses);
    }
}