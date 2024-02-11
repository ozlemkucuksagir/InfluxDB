package com.example.influxdb.connection;
import com.example.influxdb.*;

public class InfluxDBConnectionClass {
    private String url;
    private String token;
    private String bucket;
    private String org;

    public InfluxDBClient buildConnection(String url, String token, String bucket, String org) {
        this.url = url;
        this.token = token;
        this.bucket = bucket;
        this.org = org;
        return createConnection();
    }

    private InfluxDBClient createConnection() {
        return InfluxDBClientFactory.create(url, token.toCharArray(), org, bucket);
    }
}
