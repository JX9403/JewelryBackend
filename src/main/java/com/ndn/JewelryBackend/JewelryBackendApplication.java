package com.ndn.JewelryBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableJpaAuditing

public class JewelryBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(JewelryBackendApplication.class, args);
	}

}
