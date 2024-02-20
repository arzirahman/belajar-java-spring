package com.project.ordermakanan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class OrdermakananApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrdermakananApplication.class, args);
	}

}
