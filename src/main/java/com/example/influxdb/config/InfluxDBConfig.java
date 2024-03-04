package com.example.influxdb.config;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import jakarta.annotation.PostConstruct;
import lombok.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;
@Configuration
@NoArgsConstructor
@Repository
public class InfluxDBConfig {
    @Value("${influxdb.url}")
    private String url;

    @Value("${influxdb.token}")
    private String token;

    @Value("${influxdb.organization}")
    private String org;

    @Value("${influxdb.bucket}")
    private String bucket;

    InfluxDBClient influxDBClient;

    @PostConstruct
    public void init() {
        connect();
    }

    public void connect() {
        char[] tokenArray = token.toCharArray();
        influxDBClient = InfluxDBClientFactory.create(url, tokenArray, org, bucket);
    }

    public void disconnect() {
        if (influxDBClient != null) {
            influxDBClient.close();
        }
    }

    public InfluxDBClient getInfluxDB() {

        return influxDBClient;

    }
}
