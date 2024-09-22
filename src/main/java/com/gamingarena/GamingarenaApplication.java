package com.gamingarena;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GamingarenaApplication {

	public static void main(String[] args) {
		SpringApplication.run(GamingarenaApplication.class, args);
	}

}
