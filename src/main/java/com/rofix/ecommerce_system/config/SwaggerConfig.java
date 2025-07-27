package com.rofix.ecommerce_system.config;

import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/**
 * info.application.name=Ecommerce System
 * info.application.description=API for managing products, users, orders, cart, and more.
 * info.application.version=1.0.0
 */

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Ecommerce System")
                        .description("API for managing products, users, orders, cart, and more.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Abdessamad Ait Oughenja")
                                .email("wourkout123@gmail.com"))
                );
    }
}
