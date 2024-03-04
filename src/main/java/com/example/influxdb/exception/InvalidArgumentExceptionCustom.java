package com.example.influxdb.exception;

public class InvalidArgumentExceptionCustom extends RuntimeException {

    public InvalidArgumentExceptionCustom(String message) {
        super(message);
    }
}
