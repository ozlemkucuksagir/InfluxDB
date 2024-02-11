package com.example.influxdb.controller;

import com.example.influxdb.service.InfluxDBQueryService;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@RestController
public class InfluxDBQueryController {

    private final InfluxDBQueryService influxDBQueryService;

    @Autowired
    public InfluxDBQueryController(InfluxDBQueryService influxDBQueryService) {
        this.influxDBQueryService = influxDBQueryService;
    }

    @GetMapping("/query")
    public void queryData(HttpServletResponse response) throws IOException {
        String measurement = "_measurement"; // Değiştirmeniz gereken ölçüm adı
        QueryResult queryResult = influxDBQueryService.queryData(measurement);

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(queryResult.toString());
        out.flush();
    }
}
