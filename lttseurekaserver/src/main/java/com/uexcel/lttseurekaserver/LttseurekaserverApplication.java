package com.uexcel.lttseurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class LttseurekaserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(LttseurekaserverApplication.class, args);
	}

}
