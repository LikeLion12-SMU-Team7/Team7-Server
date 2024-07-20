package com.example.alcohol_free_day;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AlcoholFreeDayApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlcoholFreeDayApplication.class, args);
	}

}
