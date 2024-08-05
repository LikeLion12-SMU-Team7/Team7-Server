package com.example.alcohol_free_day;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@OpenAPIDefinition(servers = {
		@Server(url = "http://localhost:8080", description = "로컬 서버"),
		@Server(url = "https://www.alcoholfreeday.site", description = "술없는날 도메인"),
		@Server(url = "https://alcoholfreeday.site", description = "술없는날 도메인")
})@SpringBootApplication
@EnableJpaAuditing
public class AlcoholFreeDayApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlcoholFreeDayApplication.class, args);
	}

}
