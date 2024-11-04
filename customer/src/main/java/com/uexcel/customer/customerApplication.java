package com.uexcel.customer;

import com.uexcel.customer.info.AppBuiltInfo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")
@EnableFeignClients
@EnableConfigurationProperties(value = AppBuiltInfo.class)
public class customerApplication {
	public static void main(String[] args) {
		SpringApplication.run(customerApplication.class, args);
	}

}
