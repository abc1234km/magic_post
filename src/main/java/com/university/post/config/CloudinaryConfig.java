package com.university.post.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary getCloudinary(){
        return new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "dq7d8admh",
            "api_key", "414398576554995",
            "api_secret", "VY3NgVMVas4j8cr5gnjMGdVIxOg"));
    }
}
