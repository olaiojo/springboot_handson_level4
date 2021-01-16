package com.daigo.springboot_handson_4.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class LocalSearchConfig {
    @Value("${localsearch.host}")
    private String host;
    @Value("${localsearch.path}")
    private String path;
}
