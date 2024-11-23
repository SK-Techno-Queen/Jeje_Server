package com.jeju_server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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
        List<?> results = busRepository.findBusDetails();
        List<BusDetailsDto> response = results.stream()
                .map(result -> {
                    Object[] row = (Object[]) result;
                    return new BusDetailsDto(
                            (String) row[0],
                            (String) row[1],
                            (Long) row[2],
                            (String) row[3],
                            (Double) row[4],
                            (Double) row[5],
                            (String) row[6],
                            (String) row[7]
                    );
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/bus/marker")
    public ResponseEntity<List<Bus>> gatAll() {
        List<Bus> buses = busRepository.findTop50ByOrderByTimestampDesc();
        return ResponseEntity.ok(buses);
    }
    @GetMapping("/stop/broker/{ip}")
    public ResponseEntity<?> stopBroker(@PathVariable("ip") String ip) {
        try {
            // EC2 인스턴스의 IP와 명령어 설정
            String remoteCommand = "ssh -i /path/to/your-key.pem ubuntu@" + ip +
                    " /engn/confluent/scripts/stop-broker.sh";

            // ProcessBuilder를 통해 명령 실행
            ProcessBuilder builder = new ProcessBuilder();
            builder.command("sh", "-c", remoteCommand);

            // 프로세스 시작
            Process process = builder.start();

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                return ResponseEntity.ok("Command executed successfully");
            } else {
                return ResponseEntity.status(500).body("Failed to execute command. Exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/start/broker/{ip}")
    public ResponseEntity<?> startBroker(@PathVariable("ip") String ip) {
        try {
            // EC2 인스턴스의 IP와 명령어 설정
            String remoteCommand = "ssh -i /path/to/your-key.pem ubuntu@" + ip +
                    " /engn/confluent/scripts/start-broker.sh /engn/confluent/properties/broker.properties";

            // ProcessBuilder를 통해 명령 실행
            ProcessBuilder builder = new ProcessBuilder();
            builder.command("sh", "-c", remoteCommand);

            // 프로세스 시작
            Process process = builder.start();

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                return ResponseEntity.ok("Command executed successfully");
            } else {
                return ResponseEntity.status(500).body("Failed to execute command. Exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}