package com.rofix.ecommerce_system.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Value("${spring.cloud.name}")
    private String cloudName;

    @Value("${spring.cloud.api.key}")
    private String cloudApiKey;

    @Value("${spring.cloud.secret.key}")
    private String cloudSecretKey;

    @Bean
    public ModelMapper modelMapper() {
        var modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);

        return modelMapper;
    }

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", cloudApiKey,
                "api_secret", cloudSecretKey,
                "secure", true));
    }
}
