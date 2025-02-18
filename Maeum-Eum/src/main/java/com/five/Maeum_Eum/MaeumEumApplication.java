package com.five.Maeum_Eum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MaeumEumApplication {

	public static void main(String[] args) {
		SpringApplication.run(MaeumEumApplication.class, args);
	}

}
