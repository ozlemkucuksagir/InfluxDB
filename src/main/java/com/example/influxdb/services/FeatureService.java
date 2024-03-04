package com.example.influxdb.services;

import com.example.influxdb.exception.GlobalExceptionHandler;
import com.example.influxdb.exception.ResourceNotFoundException;
import com.example.influxdb.repository.FeatureRepository;
import com.example.influxdb.utility.ErrorMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Service
public class FeatureService {


    @Autowired
    FeatureRepository featureRepository;
    @Autowired
    ThingService thingService;
    private static final Logger logger = LogManager.getLogger(ValueService.class);
    String logMessage;
    FeatureService(ThingService thingService, FeatureRepository featureRepository) {
        this.thingService = thingService;
        this.featureRepository = featureRepository;
    }

    public List<String> extractBodyAsList(ResponseEntity<?> responseEntity) {
        Object body = responseEntity.getBody();
        if (body instanceof List) {
            return (List<String>) body;
        } else {
            return Collections.emptyList(); // Veya uygun bir hata fırlatılabilir.
        }
    }

    public ResponseEntity<?> getFeatureByThingsID(String thingID) {
        ResponseEntity<?> things = thingService.getAllThingsIDs();
        List<String> thingList = extractBodyAsList(things);
        if (!thingList.contains(thingID)) {
             throw new ResourceNotFoundException(ErrorMessages.ERROR_THING_NOT_FOUND + thingID);

        }

        ResponseEntity<?> featureResponse = featureRepository.getFeatureByThingsID(thingID);
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        boolean isCalledFromFeatureController = Arrays.stream(stackTraceElements).anyMatch(stackTraceElement -> stackTraceElement.getClassName().endsWith("FeatureController"));
        if (isCalledFromFeatureController) {
            logMessage = GlobalExceptionHandler.logSuccessfulResponse(featureResponse);
            logger.info(logMessage);
        }

        return featureResponse;
    }


}
