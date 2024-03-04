package com.example.influxdb.repository;

import com.example.influxdb.config.InfluxDBConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import java.util.stream.Collectors;

@Repository
public class PropertyRepository {

    @Autowired
    private InfluxDBConfig influxDBConfig;

    public ResponseEntity<?> getAllPropertiesByFeatureIDandThingsID(String thingID, String featureID) {
        String query = "import \"influxdata/influxdb/schema\" "
                + "schema.measurementFieldKeys(bucket: \"" + thingID + "\", measurement: \"" + featureID + "\")";

        return ResponseEntity.ok().body(influxDBConfig.getInfluxDB().getQueryApi()
                .query(query)
                .stream()
                .flatMap(table -> table.getRecords().stream())
                .map(record -> record.getValueByKey("_value").toString())
                .collect(Collectors.toList()));
    }
}
