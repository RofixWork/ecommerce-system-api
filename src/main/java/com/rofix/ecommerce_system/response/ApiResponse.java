package com.rofix.ecommerce_system.response;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class ApiResponse {
    private String message;
    private boolean success;
    private Instant timestamp = Instant.now();

    public ApiResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
}
