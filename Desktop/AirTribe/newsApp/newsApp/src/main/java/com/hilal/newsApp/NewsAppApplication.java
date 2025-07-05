package com.hilal.newsApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class NewsAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewsAppApplication.class, args);
	}

}
