package com.example.influxdb.service;
import com.example.influxdb.component.InfluxDBConfiguration;
import org.influxdb.InfluxDB;

import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

import org.springframework.stereotype.Service;

@Service
public class InfluxDBQueryService {

    private final InfluxDB influxDB;

    public InfluxDBQueryService(InfluxDBConfiguration influxDBConfiguration) {
        this.influxDB = influxDBConfiguration.getInfluxDB();
    }

    public QueryResult queryData(String measurement) {
        String query = "SELECT * FROM " + measurement;
        return influxDB.query(new Query(query));
    }
}
