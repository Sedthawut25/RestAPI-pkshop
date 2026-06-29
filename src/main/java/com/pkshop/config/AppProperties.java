package com.pkshop.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.List;

@ConfigurationProperties(prefix = "app.jwt")
public record AppProperties(
        String issuer,
        String secret,
        long accessTokenMinutes
) {}
