// InfluxDBService.java
package com.example.influxdb.services;

import java.util.Random;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.QueryApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.List;


@Service
public class InfluxDBServiceOld {
    InfluxDBClient influxDBClient;
    private static char[] token = "a8Y3QSr6flRI2r4TJlxUtZsSCeG0f87pwtYIa_kSsjJsqSAshnej8cHTgip3qODTUFsoY-rH0s34LLoVIVyziQ==".toCharArray();
    private static String org = "newOrg";
    private static String bucket = "innova";

    public InfluxDBServiceOld() {
        influxDBClient = InfluxDBClientFactory.create("http://localhost:8086", token, org, bucket);
    }


    public void writeData() {
        Random rand = new Random();
        double randomValue = rand.nextDouble() * 100;
        Point point = Point.measurement("temperature")
                .addTag("location", "west")
                .addField("value", randomValue)
                .time(Instant.now().toEpochMilli(), WritePrecision.MS);

        influxDBClient.getWriteApiBlocking().writePoint(point);
    }

    public void queryData() {
        String flux = "from(bucket:\"innova\") |> range(start: 0)";

        QueryApi queryApi = influxDBClient.getQueryApi();
        queryApi.query(flux).forEach(fluxTable -> fluxTable.getRecords().forEach(fluxRecord -> {
            System.out.println(fluxRecord.getTime() + ": " + fluxRecord.getValueByKey("_value"));
        }));
    }

    public Flux<ServerSentEvent<String>> streamEvents() {
        return Flux.interval(Duration.ofSeconds(10))
                .map(sequence -> ServerSentEvent.<String>builder()
                        .id(String.valueOf(sequence))
                        .event("periodic-event")
                        .data("SSE - " + LocalTime.now())
                        .build());
    }

    public Flux<ServerSentEvent<String>> performDownsamplingAndStreamEvents() {
        String flux = "from(bucket:\"innova\") " +
                "|> range(start: -1m) " +
                "|> aggregateWindow(every: 10s, fn: mean, createEmpty: false)";
        QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> queryResultList = queryApi.query(flux);
        Flux<FluxRecord> fluxRecordFlux = Flux.fromIterable(queryResultList)
                .flatMap(fluxTable -> Flux.fromIterable(fluxTable.getRecords()));
        Flux<FluxRecord> downsampledFlux = Flux.interval(Duration.ofSeconds(10))
                .zipWith(fluxRecordFlux)
                .map(tuple -> tuple.getT2());
        return downsampledFlux.map(fluxRecord -> ServerSentEvent.<String>builder()
                        .data(fluxRecord.getTime() + ": " + fluxRecord.getValueByKey("_value"))
                        .build())
                .zipWith(Flux.interval(Duration.ofSeconds(10)), (event, interval) -> event);
    }

    public Flux<ServerSentEvent<String>> performDownsamplingAndStreamEventsJson(String bucketName, String start, String every) {
        Duration everyDuration = Duration.parse(every);
        String flux = String.format("from(bucket:\"%s\") |> range(start: %s) |> aggregateWindow(every: %s, fn: mean, createEmpty: false)", bucketName, start, everyDuration.getSeconds());
        List<FluxTable> queryResultList = influxDBClient.getQueryApi().query(flux);
        Flux<FluxRecord> fluxRecordFlux = Flux.fromIterable(queryResultList)
                .flatMap(fluxTable -> Flux.fromIterable(fluxTable.getRecords()));
        Flux<FluxRecord> downsampledFlux = Flux.interval(everyDuration)
                .zipWith(fluxRecordFlux)
                .map(tuple -> tuple.getT2());

        return downsampledFlux.map(fluxRecord -> ServerSentEvent.<String>builder()
                        .data(fluxRecord.getTime() + ": " + fluxRecord.getValueByKey("_value"))
                        .build())
                .zipWith(Flux.interval(everyDuration), (event, interval) -> event);
    }

}
