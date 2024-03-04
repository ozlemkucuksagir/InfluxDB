package com.example.influxdb.repository;

import com.example.influxdb.config.InfluxDBConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import java.util.stream.Collectors;

@Repository

public class FeatureRepository {

    @Autowired
    private InfluxDBConfig influxDBConfig;

    public ResponseEntity<?> getFeatureByThingsID(String thingID) {
        String query = "import \"influxdata/influxdb/schema\" "
                + "schema.measurements(bucket: \"" + thingID + "\")";
        return ResponseEntity.ok().body(influxDBConfig.getInfluxDB().getQueryApi()
                .query(query)
                .stream()
                .flatMap(table -> table.getRecords().stream())
                .map(record -> record.getValueByKey("_value").toString())
                .collect(Collectors.toList()));
    }

}
