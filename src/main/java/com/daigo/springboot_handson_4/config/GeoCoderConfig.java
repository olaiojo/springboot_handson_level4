package com.daigo.springboot_handson_4.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 *
 */
@Getter
@Configuration
public class GeoCoderConfig {
    @Value("${geocoder.host}")
    private String host;
    @Value("${geocoder.path}")
    private String path;
}
