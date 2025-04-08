package com.example.banvemaybay.configs;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DotenvConfig {
    private static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing() // tránh lỗi nếu chưa có file .env
            .load();

    public static String get(String key) {
        return dotenv.get(key);
    }

    @PostConstruct
    public void init() {
        dotenv.entries().forEach(entry -> {
            System.setProperty(entry.getKey(), entry.getValue());
        });
    }
}
