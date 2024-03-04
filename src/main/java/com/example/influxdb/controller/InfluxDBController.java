package com.example.influxdb.controller;
import com.example.influxdb.services.InfluxDBServiceOld;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class InfluxDBController {

    private final InfluxDBServiceOld influxDBServiceOld;

    @Autowired
    public InfluxDBController(InfluxDBServiceOld influxDBServiceOld) {
        this.influxDBServiceOld = influxDBServiceOld;
    }

    @GetMapping("/write")
    public void writeData() {
        influxDBServiceOld.writeData();
    }

    @GetMapping("/query")
    public void queryData() {
        influxDBServiceOld.queryData();
    }

    @GetMapping("/stream-sse")
    public void streamEvents() {
        Flux<ServerSentEvent<String>> eventFlux = influxDBServiceOld.streamEvents();
        eventFlux.subscribe(event -> {
            System.out.println("Received event: " + event.toString());
            System.out.println("Received event: " + event.data());
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/downsampling-events")
    public Flux<ServerSentEvent<String>> streamDownsamplingEvents() {
        return influxDBServiceOld.performDownsamplingAndStreamEvents();
    }

    @GetMapping("/downsampling-eventsJson/{bucketName}/{start}/{every}")
    public Flux<ServerSentEvent<String>> streamData(
            @PathVariable(name = "bucketName") String bucketName,
            @PathVariable(name = "start") String start,
            @PathVariable(name = "every") String every) {
        return influxDBServiceOld.performDownsamplingAndStreamEventsJson(bucketName, start, every);
    }
}