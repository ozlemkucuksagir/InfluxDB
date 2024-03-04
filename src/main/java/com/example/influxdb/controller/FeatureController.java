package com.example.influxdb.controller;

import com.example.influxdb.services.FeatureService;
import com.example.influxdb.services.ValueService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeatureController {

    FeatureService featureService;
    private static final Logger logger = LogManager.getLogger(ValueService.class);

    @Autowired
    FeatureController(FeatureService featureService) {
        this.featureService = featureService;
    }

    @GetMapping(value = {"/api/things/{thingId}/features", "/api/things/{thingId}/features/"})
    public ResponseEntity<?> getAllFeaturewithThingsID(@PathVariable("thingId") String thingID) {
        logger.info("URL:/api/things/" + thingID + "/features;Request Success");
        return ResponseEntity.ok().body(featureService.getFeatureByThingsID(thingID)).getBody();
    }
}
