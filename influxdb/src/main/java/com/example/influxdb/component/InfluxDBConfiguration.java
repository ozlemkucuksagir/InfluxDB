package com.example.influxdb.component;

import jakarta.annotation.PostConstruct;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InfluxDBConfiguration {

    private InfluxDB influxDB;

    @Value("${influxdb.url}")
    private String url;

    @Value("${influxdb.username}")
    private String username;

    @Value("${influxdb.password}")
    private String password;

    public InfluxDBConfiguration() {
        this.influxDB = null; // initialize as null
    }

    @PostConstruct
    public void initialize() {
        this.influxDB = InfluxDBFactory.connect(url, username, password);
        this.influxDB.setDatabase("2");
    }

    public InfluxDB getInfluxDB() {
        return influxDB;
    }
}
