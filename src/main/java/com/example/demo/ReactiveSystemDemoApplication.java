package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@SpringBootApplication
@EnableR2dbcRepositories(basePackages = "com.example.demo.repository")
public class ReactiveSystemDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveSystemDemoApplication.class, args);
	}

}
