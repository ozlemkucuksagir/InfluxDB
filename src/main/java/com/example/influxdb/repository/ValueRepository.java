package com.example.influxdb.repository;

import com.example.influxdb.config.InfluxDBConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Repository
public class ValueRepository {
    @Autowired
    private InfluxDBConfig influxDBConfig;

    // LocalDateTime to ZonedDateTime
    // e.g. "2024-02-29T16:17:50" -> "2024-02-29T13:17:50Z"
    private String local2Zoned(String time) {
        return LocalDateTime.parse(time)
                            .atZone(ZoneId.systemDefault())
                            .withZoneSameInstant(ZoneId.of("UTC"))
                            .format(DateTimeFormatter.ISO_INSTANT)
                            .toString();
    }

    public ResponseEntity<?> getValue(String thingID, String featureID, String propertyPath, String startTime, String stopTime, String interval) {
        if (stopTime != null){
            startTime = local2Zoned(startTime);
            stopTime = local2Zoned(stopTime);
        }


        String query = "from(bucket: \"" + thingID + "\") "
                + "|> range(start: " + startTime;

        // Hem stopTime hem de interval parametreleri verilmişse
        if (stopTime != null && interval != null) {
            query += ", stop: " + stopTime
                    + ") "
                    + "|> aggregateWindow(every: " + interval + ", fn: mean, createEmpty: false)";
        }
        // Sadece stopTime parametresi verilmişse
        else if (stopTime != null) {
            query += ", stop: " + stopTime + ")";
        }
        // Sadece interval parametresi verilmişse
        else if (interval != null) {
            query += ") "
                    + "|> aggregateWindow(every: " + interval + ", fn: mean, createEmpty: false)";
        }
        // Hiçbir ek parametre verilmediyse
        else {
            query += ")";
        }

        query += "|> filter(fn: (r) => r[\"_measurement\"] == \"" + featureID + "\") "
                + "|> filter(fn: (r) => r[\"_field\"] == \"" + propertyPath + "\") "
                + "|> yield()";

        return ResponseEntity.ok().body(influxDBConfig.getInfluxDB().getQueryApi()
                .query(query)
                .stream()
                .flatMap(table -> table.getRecords().stream())
                .map(record -> new String[]{record.getTime().toString(), record.getValueByKey("_value").toString()})
                .collect(Collectors.toList()));
    }

}
