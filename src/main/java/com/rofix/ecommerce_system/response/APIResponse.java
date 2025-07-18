package com.rofix.ecommerce_system.response;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class APIResponse {
    private String message;
    private boolean success;
    private Instant timestamp = Instant.now();

    public APIResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
}
