package com.example.banvemaybay.configs;

import com.cloudinary.Cloudinary;
import com.example.banvemaybay.utils.EnvUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

//    private final String cloudName = System.getProperty("CLOUDINARY_NAME");
//    private final String apiKey = System.getProperty("CLOUDINARY_KEY");
//    private final String apiSecret = System.getProperty("CLOUDINARY_SECRET");

//    private final String cloudName = System.getenv("CLOUDINARY_NAME");
//    private final String apiKey = System.getenv("CLOUDINARY_KEY");
//    private final String apiSecret = System.getenv("CLOUDINARY_SECRET");

    private final String cloudName = EnvUtil.get("CLOUDINARY_NAME");
    private final String apiKey = EnvUtil.get("CLOUDINARY_KEY");
    private final String apiSecret = EnvUtil.get("CLOUDINARY_SECRET");

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);
        return new Cloudinary(config);
    }
}

