package com.example.influxdb.utility;

import com.example.influxdb.exception.InvalidArgumentExceptionCustom;

import java.time.LocalDateTime;

public class ValidationTime {
    public static void validate(String time){
        String pattern = "^(-)?\\d+[smhdwMy]$";
        if (!time.matches(pattern)){
            throw new InvalidArgumentExceptionCustom(ErrorMessages.ERROR_INVALID_START_TIME + time);
        }
    }

    public static void validate(String startTime, String stopTime){
        if (stopTime == null){
            validate(startTime);
            return;
        }
        LocalDateTime start, stop;
        // Check start time format
        try {
            start = LocalDateTime.parse(startTime);
        } catch (Exception e) {
            throw new InvalidArgumentExceptionCustom(ErrorMessages.ERROR_INVALID_START_TIME + startTime);
        }
        // Check stop time format
        try {
            stop = LocalDateTime.parse(stopTime);
        } catch (Exception e) {
            throw new InvalidArgumentExceptionCustom(ErrorMessages.ERROR_INVALID_STOP_TIME + stopTime);
        }
        // Check start time and stop time is valid
        if (stop.isBefore(start) || stop.isEqual(start) || startTime.equals(stopTime)) {
            throw new InvalidArgumentExceptionCustom(ErrorMessages.ERROR_INVALID_TIMES_INTERVAL + "Start Time: " + startTime + " || Stop Time:" + stopTime);
        }
    }

}
