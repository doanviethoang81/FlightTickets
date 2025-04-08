package com.example.banvemaybay.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**") // Định nghĩa API nào cần CORS
                        .allowedOrigins("http://localhost:5173") // Cho phép từ FE
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowCredentials(true) // Cho phép gửi cookie
                        .exposedHeaders("Location"); // Quan trọng: Expose `Location`
            }
        };
    }
}
