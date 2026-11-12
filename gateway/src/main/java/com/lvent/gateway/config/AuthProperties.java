package com.lvent.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "lvent.auth")
public class AuthProperties {
    private List<String> includePath;
    private List<String> excludePath;
}
