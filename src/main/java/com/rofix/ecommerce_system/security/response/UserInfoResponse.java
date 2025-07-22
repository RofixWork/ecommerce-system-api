package com.rofix.ecommerce_system.security.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfoResponse {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private boolean enabled;
    private List<String> roles;

    public UserInfoResponse(Long id, String username, String email, String phone, List<String> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.roles = roles;
    }

    public UserInfoResponse(Long id, String username, List<String> roles) {
        this.id = id;
        this.roles = roles;
        this.username = username;
    }
}
