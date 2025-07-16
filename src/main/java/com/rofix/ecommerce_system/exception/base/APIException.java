package com.rofix.ecommerce_system.exception.base;

import org.springframework.http.HttpStatus;

public class APIException extends CustomException {

    public APIException(String message, HttpStatus statusCode) {
        super(message, statusCode);
    }
}
