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

    @Query(value = """
        SELECT A.PLATE_NO,\s
            MAX(A.TIMESTAMP) AS Original,\s
            MAX(A.id) AS A_Id,
            MAX(B.TIMESTAMP) AS Filter,
            B.LOCAL_Y,\s
            B.LOCAL_X,\s
            B.id AS B_Id,\s
            B.CURR_STATION_NM,\s
            B.ROUTE_NUM
        FROM bus_produce_topic A
        LEFT JOIN bus_produce_topic B ON A.id = B.id
        WHERE A.timestamp >= NOW() + INTERVAL 9 HOUR - INTERVAL 10 SECOND
        GROUP BY A.PLATE_NO, B.LOCAL_Y, B.LOCAL_X, B.id, B.CURR_STATION_NM, B.ROUTE_NUM;
    """, nativeQuery = true)
    List<?> findBusDetails();
}