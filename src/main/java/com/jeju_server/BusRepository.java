package com.jeju_server;

import com.jeju_server.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {
    @Query(value = "SELECT b.* FROM bus_test_topic3 b INNER JOIN (SELECT b2.plate_No, MAX(b2.timestamp) AS latestTimestamp FROM bus_test_topic3 b2 GROUP BY b2.plate_No) latest ON b.plate_No = latest.plate_No AND b.timestamp = latest.latestTimestamp", nativeQuery = true)
    List<Bus> findLatestBusesByPlateNo();

    List<Bus> findTop50ByOrderByTimestampDesc();
}