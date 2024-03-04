package com.example.influxdb.controller;
import com.example.influxdb.services.ThingService;
import com.example.influxdb.services.ValueService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ThingController {
    ThingService thingService;
    private static final Logger logger = LogManager.getLogger(ValueService.class);

    @Autowired
    public ThingController(ThingService thingService) {
        this.thingService = thingService;
    }

    @GetMapping(value = {"/api/things", "/api/things/"})
    public ResponseEntity<?> getThingsID() {
        logger.info("URL:/api/things/;Request Success");

        return ResponseEntity.ok().body(thingService.getAllThingsIDs()).getBody();
    }

}
