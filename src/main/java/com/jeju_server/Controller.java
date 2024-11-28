package com.jeju_server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class Controller {

    private final BusRepository busRepository;

    @Value("${visitjeju.api.key}")
    private String apiKey;

    @GetMapping("/spot")
    public ResponseEntity<String> spot() {
        String url = String.format("http://api.visitjeju.net/vsjApi/contents/searchList?apiKey=%s&locale=kr&category=c1&page=1", apiKey);
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("API 호출 중 오류 발생: " + e.getMessage());
        }
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
    public ResponseEntity<List<Bus>> getAll() {
        List<Bus> buses = busRepository.findTop50ByOrderByTimestampDesc();
        return ResponseEntity.ok(buses);
    }

    // Key 파일 경로 지정
    private static final String KEY_FILE_PATH = "/home/tomcat/key.pem";

    @GetMapping("/stop/broker/{ip}")
    public ResponseEntity<?> stopBroker(@PathVariable("ip") String ip) {
        return executeRemoteCommand(ip, "/engn/confluent/scripts/stop-broker.sh");
    }

    @GetMapping("/start/broker/{ip}")
    public ResponseEntity<?> startBroker(@PathVariable("ip") String ip) {
        return executeRemoteCommand(ip, "/engn/confluent/scripts/start-broker.sh /engn/confluent/properties/broker.properties");
    }

    @GetMapping("/stop/controller/{ip}")
    public ResponseEntity<?> stopController(@PathVariable("ip") String ip) {
        return executeRemoteCommand(ip, "/engn/confluent/scripts/stop-controller.sh");
    }

    @GetMapping("/start/controller/{ip}")
    public ResponseEntity<?> startController(@PathVariable("ip") String ip) {
        return executeRemoteCommand(ip, "/engn/confluent/scripts/start-controller.sh /engn/confluent/properties/controller.properties");
    }

    @GetMapping("/stop/connect/{ip}")
    public ResponseEntity<?> stopConnect(@PathVariable("ip") String ip) {
        return executeRemoteCommand(ip, "/engn/confluent/scripts/stop-connect.sh");
    }

    @GetMapping("/start/connect/{ip}")
    public ResponseEntity<?> startConnect(@PathVariable("ip") String ip) {
        return executeRemoteCommand(ip, "/engn/confluent/scripts/start-connect.sh /engn/confluent/properties/connect-worker.properties");
    }

    @GetMapping("/stop/sr/{ip}")
    public ResponseEntity<?> stopSR(@PathVariable("ip") String ip) {
        return executeRemoteCommand(ip, "/engn/confluent/scripts/stop-schema-registry.sh");
    }

    @GetMapping("/start/sr/{ip}")
    public ResponseEntity<?> startSR(@PathVariable("ip") String ip) {
        return executeRemoteCommand(ip, "/engn/confluent/scripts/start-schema-registry.sh /engn/confluent/properties/schema-registry.properties");
    }


    private ResponseEntity<?> executeRemoteCommand(String ip, String scriptPath) {
        try {
            File keyFile = new File(KEY_FILE_PATH);
            if (!keyFile.exists() || !keyFile.canRead()) {
                throw new IllegalArgumentException("Key file is missing or not readable: " + keyFile.getAbsolutePath());
            }

            // SSH 명령어 구성
            String remoteCommand = String.format("ssh -i %s -o StrictHostKeyChecking=no ubuntu@%s %s",
                    keyFile.getAbsolutePath(), ip, scriptPath);

            // 명령 실행
            ProcessBuilder builder = new ProcessBuilder("sh", "-c", remoteCommand);
            builder.redirectErrorStream(true); // 에러 스트림을 표준 출력으로 병합
            Process process = builder.start();

            // 명령 출력 읽기
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                StringBuilder output = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }

                int exitCode = process.waitFor();
                if (exitCode == 0) {
                    return ResponseEntity.ok("Command executed successfully:\n" + output);
                } else {
                    return ResponseEntity.status(500).body("Failed to execute command. Exit code: " + exitCode + "\nOutput:\n" + output);
                }
            }
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(500).body("Error executing remote command: " + e.getMessage());
        }
    }
}