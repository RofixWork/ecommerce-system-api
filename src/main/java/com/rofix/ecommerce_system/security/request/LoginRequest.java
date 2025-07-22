package com.rofix.ecommerce_system.security.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Please provide username")
    @Size(min = 2, max = 20, message = "username must be between 2 and 20 chars")
    private String username;

    @NotBlank(message = "Please provide password")
    @Size(min = 8, message = "password must be greater than 8 chars")
    private String password;
}
