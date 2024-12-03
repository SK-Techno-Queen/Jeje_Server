package com.jeju_server;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {
    List<Bus> findTop50ByOrderByTimestampDesc();

    @Query(value = """
        select B.PLATE_NO,
               (select C.TIMESTAMP from jeju_produce_topic C WHERE C.id = B.id) as MAX_TIMESTAMP,
               (SELECT MAX(C.TIMESTAMP) FROM jeju_search_topic C WHERE C.PLATE_NO = B.PLATE_NO) AS SEARCH_TIMESTAMP,
               (SELECT C.LOCAL_Y FROM jeju_produce_topic C WHERE C.id = B.id LIMIT 1) AS LOCAL_Y,
               (SELECT C.LOCAL_X FROM jeju_produce_topic C WHERE C.id = B.id LIMIT 1) AS LOCAL_X,
               (SELECT C.CURR_STATION_NM FROM jeju_produce_topic C WHERE C.id = B.id LIMIT 1) AS CURR_STATION,
               (SELECT C.ROUTE_NUM FROM jeju_produce_topic C WHERE C.id = B.id LIMIT 1) AS ROUTE_NUM
        from (
        select PLATE_NO, max(id) as ID
        FROM jeju_produce_topic A
        WHERE A.TIMESTAMP >= DATE_ADD(NOW(), INTERVAL 9 HOUR) - INTERVAL 5 SECOND
        GROUP BY A.PLATE_NO) B;
    """, nativeQuery = true)
    List<?> findBusDetails();
}