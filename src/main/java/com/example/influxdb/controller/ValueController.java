package com.example.influxdb.controller;
import com.example.influxdb.exception.*;
import com.example.influxdb.services.FeatureService;
import com.example.influxdb.services.PropertyService;
import com.example.influxdb.services.ThingService;
import com.example.influxdb.services.ValueService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ValueController {
    GlobalExceptionHandler globalExceptionHandler;
    ValueService valueService;
    ThingService thingService;
    FeatureService featureService;
    PropertyService propertyService;
    private static final Logger logger = LogManager.getLogger(ValueService.class);

    @Autowired
    public ValueController(ValueService valueService, ThingService thingService, FeatureService featureService, PropertyService propertyService) {
        this.valueService = valueService;
        this.thingService = thingService;
        this.featureService = featureService;
        this.propertyService = propertyService;

    }

    @GetMapping(value = {"/api/things/{thingId}/features/{featureId}/properties/{propertyPath}", "/api/things/{thingId}/features/{featureId}/properties/{propertyPath}/"})
    public ResponseEntity<?> getValue(@PathVariable("thingId") String thingID,
                                      @PathVariable("featureId") String featureID,
                                      @PathVariable("propertyPath") String propertyPath,
                                      @RequestParam("startTime") String startTime,
                                      @RequestParam(value = "stopTime", required = false) String stopTime,
                                      @RequestParam(value = "interval", required = false) String interval) {


        // InfluxDBService'den gelen propertyList'i kontrol et
        ResponseEntity<?> values = valueService.getValue(thingID, featureID, propertyPath, startTime, stopTime, interval);
        logger.info("URL:/api/things/" + thingID + "/features/"+featureID+"/properties/"+propertyPath+";Request Success");

        return ResponseEntity.ok().body(values).getBody();
    }


/*

    private ResponseEntity<?> buildErrorResponse(HttpStatus status, String message) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("status", String.valueOf(status.value()));
        errorResponse.put("error", status.getReasonPhrase());
        errorResponse.put("message", message);
        return ResponseEntity.status(status).body(errorResponse);
    }*/

}
