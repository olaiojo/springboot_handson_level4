package com.daigo.springboot_handson_4.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class YahooApiConfig {
    @Value("${app.id}")
    private String appId;
}
