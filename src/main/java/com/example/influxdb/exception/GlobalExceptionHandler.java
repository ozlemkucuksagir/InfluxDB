package com.example.influxdb.exception;

import com.example.influxdb.exception.response.C2ErrorResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(value = {ResourceNotFoundException.class, InvalidArgumentExceptionCustom.class, Exception.class})
    public ResponseEntity<C2ErrorResponse> handleSpecificExceptions(Exception ex) {
        HttpStatus status;
        String error;
        if (ex instanceof ResourceNotFoundException) {
            status = HttpStatus.NOT_FOUND;
            error = "Not Found";
        } else if (ex instanceof InvalidArgumentExceptionCustom) {
            status = HttpStatus.BAD_REQUEST;
            error = "Bad Request";
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            error = "Internal Server Error";
        }
        C2ErrorResponse response = new C2ErrorResponse(status.value(), error, ex.getMessage());
        String logMessage = "";

        logger.error("Response Status: {}; Error: {};Message: {}",
                response.getStatusCode(), response.getError(), response.getMessage());
        return ResponseEntity.status(status).body(response);
    }

    public static String logSuccessfulResponse(ResponseEntity<?> responseEntity) {
        Object status = responseEntity.getStatusCode();
        Object body = responseEntity.getBody();

        String logMessage = "";
        if (body instanceof List) {
            List<?> bodyList = (List<?>) body;
            logMessage = String.format("Response Status: %s;Body Size: %d", status, bodyList.size());
        } else if (body instanceof Object[]) {
            Object[] bodyArray = (Object[]) body;
            logMessage = String.format("Response Status: %s;Body Size: %d", status, bodyArray.length);
        } else {
            logMessage = String.format("Response Status: %s;Body Size: 1", status);
        }

        return logMessage;
    }


}