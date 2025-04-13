package com.example.banvemaybay.configs;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DotenvConfig {
    private static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing() // để local dùng file .env, còn Render thì không có file này
            .load();

    @PostConstruct
    public void init() {
        dotenv.entries().forEach(entry -> {
            // nếu chưa có biến môi trường hệ thống, thì mới set
            if (System.getenv(entry.getKey()) == null) {
                System.setProperty(entry.getKey(), entry.getValue());
            }
        });
    }
}
