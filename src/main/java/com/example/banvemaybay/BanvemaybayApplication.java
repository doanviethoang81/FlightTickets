package com.example.banvemaybay;

import com.example.banvemaybay.configs.EnvLoader;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BanvemaybayApplication {

	public static void main(String[] args) {
		EnvLoader.loadEnv();

		SpringApplication.run(BanvemaybayApplication.class, args);
	}
}
