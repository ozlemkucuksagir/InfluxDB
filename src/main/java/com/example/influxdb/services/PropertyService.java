package com.example.influxdb.services;

import com.example.influxdb.exception.GlobalExceptionHandler;
import com.example.influxdb.exception.ResourceNotFoundException;
import com.example.influxdb.repository.PropertyRepository;
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
public class PropertyService {
    @Autowired
    FeatureService featureService;
    @Autowired
    PropertyRepository propertyRepository;
    private static final Logger logger = LogManager.getLogger(ValueService.class);
    String logMessage;
    public List<String> extractBodyAsList(ResponseEntity<?> responseEntity) {
        Object body = responseEntity.getBody();
        if (body instanceof List) {
            return (List<String>) body;
        } else {
            return Collections.emptyList();
        }
    }

    public ResponseEntity<?> getAllPropertiesByFeatureIdandThingsID(String thingID, String featureID) {

        ResponseEntity<?> features = featureService.getFeatureByThingsID(thingID);
        List<String> featureList = extractBodyAsList(features);
        if (!featureList.contains(featureID)) {
            throw new ResourceNotFoundException(ErrorMessages.ERROR_FEATURE_NOT_FOUND + featureID);
        }

        ResponseEntity<?> propertyResponse = propertyRepository.getAllPropertiesByFeatureIDandThingsID(thingID, featureID);
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        boolean isCalledFromPropertyController = Arrays.stream(stackTraceElements)
                .anyMatch(stackTraceElement -> stackTraceElement.getClassName().endsWith("PropertyController"));
        if (isCalledFromPropertyController) {
            logMessage=GlobalExceptionHandler.logSuccessfulResponse(propertyResponse);
            logger.info(logMessage);

        }
        return propertyResponse;
    }


}
