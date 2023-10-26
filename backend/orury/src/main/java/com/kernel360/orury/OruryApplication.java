package com.kernel360.orury;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class OruryApplication {

	public static void main(String[] args) {
		SpringApplication.run(OruryApplication.class, args);
	}

}
