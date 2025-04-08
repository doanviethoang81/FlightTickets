package com.example.banvemaybay;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BanvemaybayApplication {

	public static void main(String[] args) {
		String dbUrl = System.getenv("SPRING_DATASOURCE_URL");
		String dbUser = System.getenv("SPRING_DATASOURCE_USERNAME");
		String dbPass = System.getenv("SPRING_DATASOURCE_PASSWORD");

		if (dbUrl == null || dbUser == null || dbPass == null) {
			Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
			dbUrl = dbUrl != null ? dbUrl : dotenv.get("SPRING_DATASOURCE_URL");
			dbUser = dbUser != null ? dbUser : dotenv.get("SPRING_DATASOURCE_USERNAME");
			dbPass = dbPass != null ? dbPass : dotenv.get("SPRING_DATASOURCE_PASSWORD");
		}

		System.setProperty("SPRING_DATASOURCE_URL", dbUrl);
		System.setProperty("SPRING_DATASOURCE_USERNAME", dbUser);
		System.setProperty("SPRING_DATASOURCE_PASSWORD", dbPass);

		if (dbUrl == null || dbUser == null || dbPass == null) {
			Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
			dbUrl = dbUrl != null ? dbUrl : dotenv.get("SPRING_DATASOURCE_URL");
			dbUser = dbUser != null ? dbUser : dotenv.get("SPRING_DATASOURCE_USERNAME");
			dbPass = dbPass != null ? dbPass : dotenv.get("SPRING_DATASOURCE_PASSWORD");
		}

		System.setProperty("SPRING_DATASOURCE_URL", dbUrl);
		System.setProperty("SPRING_DATASOURCE_USERNAME", dbUser);
		System.setProperty("SPRING_DATASOURCE_PASSWORD", dbPass);

		SpringApplication.run(BanvemaybayApplication.class, args);
	}
}
