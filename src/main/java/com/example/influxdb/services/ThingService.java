// InfluxDBService.java
package com.example.influxdb.services;

import com.example.influxdb.exception.GlobalExceptionHandler;
import com.example.influxdb.repository.ThingRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;


@Service
public class ThingService {

    @Autowired
    ThingRepository thingRepository;
    private static final Logger logger = LogManager.getLogger(ValueService.class);
    String logMessage;
    ThingService(ThingRepository thingRepository) {
        this.thingRepository = thingRepository;
    }

    public ResponseEntity<?> getAllThingsIDs() {
        ResponseEntity<?> thingsResponse = thingRepository.getAllThingsIDs();
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        boolean isCalledFromThingController = Arrays.stream(stackTraceElements)
                .anyMatch(stackTraceElement -> stackTraceElement.getClassName().endsWith("ThingController"));
        if (isCalledFromThingController) {
            logMessage= GlobalExceptionHandler.logSuccessfulResponse(thingsResponse);
            logger.info(logMessage);

        }
        return thingsResponse;
    }


}
