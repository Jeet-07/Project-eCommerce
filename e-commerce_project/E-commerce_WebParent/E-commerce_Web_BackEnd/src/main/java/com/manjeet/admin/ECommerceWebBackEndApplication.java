package com.manjeet.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.manjeet.common.entity")
public class ECommerceWebBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(ECommerceWebBackEndApplication.class, args);
	}

}
