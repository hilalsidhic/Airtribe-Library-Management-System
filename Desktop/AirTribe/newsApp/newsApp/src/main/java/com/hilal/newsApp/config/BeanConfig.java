package com.hilal.newsApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class BeanConfig {
    // This is for internal calls (if needed)
    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }

    // ✅ This is a clean client for NewsAPI — no JWT, no auth
    @Bean(name = "externalNewsApiWebClient")
    public WebClient externalNewsApiWebClient() {
        return WebClient.builder()
                .filter((request, next) -> {
                    System.out.println("Outgoing request: " + request.url());
                    request.headers().forEach((k, v) -> System.out.println(k + ": " + v));
                    return next.exchange(request);
                })
                .build();
    }

}

