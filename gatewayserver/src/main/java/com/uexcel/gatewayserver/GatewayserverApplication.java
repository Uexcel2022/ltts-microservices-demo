package com.uexcel.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
public class GatewayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}

	@Bean
	public RouteLocator rewritePath(RouteLocatorBuilder rb){

		return rb.routes()
				.route(p->p.path("/saferide/customer/**")
				.filters(f->f.rewritePath("/saferide/customer/(?<segment>.*)","/${segment}")
						.addResponseHeader("X-Date",getTime())
						.circuitBreaker(config -> config.setName("customerCircuitBreaker")
						.setFallbackUri("forward:/contactSupport")))
						.uri("lb://CUSTOMER"))

				.route(p->p.path("/saferide/bus/**")
						.filters(f->f.rewritePath("/saferide/bus/(?<segment>.*)","/${segment}"))
						.uri("lb://BUS"))

				.route(p->p.path("/saferide/ticketing/**")
						.filters(f->f.rewritePath("/saferide/ticketing/(?<segment>.*)","/${segment}")
								.circuitBreaker(config -> config.setName("ticketingCircuitBreaker")
										.setFallbackUri("forward:/contactSupport")))
						.uri("lb://TICKETING"))

				.build();

	}

	private String getTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSZ");
		return sdf.format(new Date());
	}
}
