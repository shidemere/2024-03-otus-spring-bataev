package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "ru.otus.hw.repository")
@EntityScan("ru.otus.hw.models")
public class Homework07 {

	public static void main(String[] args) {
		SpringApplication.run(Homework07.class, args);
	}

}
