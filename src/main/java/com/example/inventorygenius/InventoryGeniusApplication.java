package com.example.inventorygenius;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.session.HttpSessionEventPublisher;


@SpringBootApplication
public class InventoryGeniusApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryGeniusApplication.class, args);
	}

	
	@Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
    }
	
}
