package com.rofix.ecommerce_system.security.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String firstName;

    @NotBlank
    @Size(min = 3, max = 20)
    private String lastName;

    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @Email
    private String email;

    @NotBlank(message = "Phone Number cannot be blank!!!")
    @Pattern(regexp = "^\\+?[\\d\\s\\-\\(\\)]{7,20}$", message = "Phone number must be valid format")
    @Size(max = 15)
    private String phone;

    @NotBlank
    @Size(min = 3, max = 40)
    private String password;

    @NotEmpty(message = "At least one Role must be added!")
    private Set<String> roles;

}
