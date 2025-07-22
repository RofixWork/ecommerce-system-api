package com.rofix.ecommerce_system.security.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Username is required.")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters.")
    private String username;

    @NotBlank(message = "Email is required.")
    @Email(message = "Please enter a valid email address.")
    private String email;

    @NotBlank(message = "Phone number is required.")
    @Pattern(regexp = "^\\+?[\\d\\s\\-\\(\\)]{7,20}$", message = "Phone number must be in a valid format (e.g., +1234567890).")
    @Size(max = 20, message = "Phone number cannot exceed 20 characters.")
    private String phone;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, max = 40, message = "Password must be between 8 and 40 characters.")
    private String password;

    //    @NotEmpty(message = "At least one role must be selected.")
    private Set<@Pattern(regexp = "(?i)^(CUSTOMER|SELLER)$", message = "Invalid role value. Allowed roles are CUSTOMER, SELLER.") String> roles;
}