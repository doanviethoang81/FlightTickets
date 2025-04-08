package com.example.banvemaybay.configs;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvLoader {

    public static void loadEnv() {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        // Load Database
        setEnvIfMissing("SPRING_DATASOURCE_URL", dotenv);
        setEnvIfMissing("SPRING_DATASOURCE_USERNAME", dotenv);
        setEnvIfMissing("SPRING_DATASOURCE_PASSWORD", dotenv);

        // Google OAuth
        setEnvIfMissing("GOOGLE_CLIENT_ID", dotenv);
        setEnvIfMissing("GOOGLE_CLIENT_SECRET", dotenv);

        // VNPay
        setEnvIfMissing("VNPAY_TMN_CODE", dotenv);
        setEnvIfMissing("VNPAY_HASH_SECRET", dotenv);
    }

    private static void setEnvIfMissing(String key, Dotenv dotenv) {
        String value = System.getenv(key);
        if (value == null) {
            value = dotenv.get(key);
            if (value != null) {
                System.setProperty(key, value);
            }
        }
    }
}

