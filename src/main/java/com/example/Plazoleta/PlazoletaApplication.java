package com.example.Plazoleta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class PlazoletaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlazoletaApplication.class, args);
	}

}
