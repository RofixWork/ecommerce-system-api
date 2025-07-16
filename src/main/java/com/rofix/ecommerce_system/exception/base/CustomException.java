package com.rofix.ecommerce_system.exception.base;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public abstract class CustomException extends RuntimeException {
    @Getter
    private final HttpStatus statusCode;

    public CustomException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
