package com.wjlee.kpmoney;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class KpMoneyApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(KpMoneyApiApplication.class, args);
	}

}
