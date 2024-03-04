package com.example.influxdb.exception.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class CustomErrorResponse {
    private HttpStatus status;
    private String error;
    private String message;


    public CustomErrorResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
        this.error = status.getReasonPhrase();
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }


}