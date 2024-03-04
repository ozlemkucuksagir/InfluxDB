package com.example.influxdb.repository;


import com.example.influxdb.config.InfluxDBConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import java.util.stream.Collectors;


@Repository
public class ThingRepository {
    @Autowired
    private InfluxDBConfig influxDBConfig;

    public ResponseEntity<?> getAllThingsIDs() {
        return ResponseEntity.ok().body(influxDBConfig.getInfluxDB().getQueryApi()
                .query("buckets()")
                .stream()
                .flatMap(table -> table.getRecords().stream())
                .map(record -> record.getValueByKey("name").toString())
                .collect(Collectors.toList()));
    }
}
