package com.zzyl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ZzylApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZzylApplication.class, args);
	}
}
