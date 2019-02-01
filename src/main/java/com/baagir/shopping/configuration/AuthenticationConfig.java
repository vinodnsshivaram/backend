package com.baagir.shopping.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationConfig {
    private final Boolean enabled;

    @Autowired
    public AuthenticationConfig(@Value("${authn.enabled}") String enabled) {
        this.enabled = !(enabled != null && !enabled.isEmpty()) || Boolean.parseBoolean(enabled);
    }
}
