package com.jeju_server;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {
    List<Bus> findTop50ByOrderByTimestampDesc();

    @Query(value = """
    SELECT
        A.PLATE_NO,
        MAX(A.TIMESTAMP) AS MAX_TIMESTAMP,
        MAX(A.id) AS MAX_ID,
        (SELECT MAX(B.TIMESTAMP) FROM jeju_search_topic B WHERE A.PLATE_NO = B.PLATE_NO) AS SEARCH_TIMESTAMP,
        (SELECT B.LOCAL_Y FROM jeju_produce_topic B WHERE A.id = B.id LIMIT 1) AS LOCAL_Y,
        (SELECT B.LOCAL_X FROM jeju_produce_topic B WHERE A.id = B.id LIMIT 1) AS LOCAL_X,
        (SELECT B.CURR_STATION_NM FROM jeju_produce_topic B WHERE A.id = B.id LIMIT 1) AS CURR_STATION,
        (SELECT B.ROUTE_NUM FROM jeju_produce_topic B WHERE A.id = B.id LIMIT 1) AS ROUTE_NUM
    FROM jeju_produce_topic A
    WHERE A.TIMESTAMP >= DATE_ADD(NOW(), INTERVAL 9 HOUR) - INTERVAL 2 SECOND
    GROUP BY A.PLATE_NO, A.id;
    """, nativeQuery = true)
    List<?> findBusDetails();
}