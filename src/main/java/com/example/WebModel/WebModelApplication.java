package com.example.WebModel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;

@SpringBootApplication
@ConfigurationProperties(prefix="my")
public class WebModelApplication { // base class to run the application

	public static void main(String[] args) {
		SpringApplication.run(WebModelApplication.class, args);
	}

}
