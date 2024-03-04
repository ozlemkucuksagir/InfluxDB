package com.example.influxdb.services;

import com.example.influxdb.exception.GlobalExceptionHandler;
import com.example.influxdb.exception.ResourceNotFoundException;
import com.example.influxdb.repository.ValueRepository;
import com.example.influxdb.utility.ErrorMessages;
import com.example.influxdb.utility.ValidationTime;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class ValueService {

    @Autowired
    ValueRepository valueRepository;
    @Autowired
    PropertyService propertyService;
    private static final Logger logger = LogManager.getLogger(ValueService.class);
    String logMessage;
    public List<String> extractBodyAsList(ResponseEntity<?> responseEntity) {
        Object body = responseEntity.getBody();
        if (body instanceof List) {
            return (List<String>) body;
        } else {
            return Collections.emptyList(); // Veya uygun bir hata fırlatılabilir.
        }
    }

    public ResponseEntity<?> getValue(String thingID, String featureID, String propertyPath, String startTime, String stopTime, String interval) {
        ResponseEntity<?> properties = propertyService.getAllPropertiesByFeatureIdandThingsID(thingID, featureID);
        List<String> propertyList = extractBodyAsList(properties);
        if (!propertyList.contains(propertyPath)) {
            throw new ResourceNotFoundException(ErrorMessages.ERROR_PROPERTY_NOT_FOUND + propertyPath);
        }

        if (startTime == null) {
            throw new ResourceNotFoundException(ErrorMessages.ERROR_START_TIME_NOT_FOUND);
        }

        ValidationTime.validate(startTime, stopTime);

        ResponseEntity<?> valueResponse = valueRepository.getValue(thingID, featureID, propertyPath, startTime, stopTime, interval);
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        boolean isCalledFromValueController = Arrays.stream(stackTraceElements)
                .anyMatch(stackTraceElement -> stackTraceElement.getClassName().endsWith("ValueController"));
        if (isCalledFromValueController) {
           logMessage= GlobalExceptionHandler.logSuccessfulResponse(valueResponse);
           logger.info(logMessage);
        }
        return valueResponse;
    }
}
