package com.example.influxdb.controller;
import com.example.influxdb.services.PropertyService;
import com.example.influxdb.services.ValueService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PropertyController {
    PropertyService propertyService;
    private static final Logger logger = LogManager.getLogger(ValueService.class);

    @Autowired
    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping(value = {"/api/things/{thingId}/features/{featureId}/properties", "/api/things/{thingId}/features/{featureId}/properties/"})
    public ResponseEntity<?> getAllPropertiesByFeatureIdandThingsID(@PathVariable("thingId") String thingID, @PathVariable("featureId") String featureID) {
        logger.info("URL:/api/things/" + thingID + "/features/"+featureID+"/properties;Request Success");

        return ResponseEntity.ok().body(propertyService.getAllPropertiesByFeatureIdandThingsID(thingID, featureID)).getBody();
    }
}

