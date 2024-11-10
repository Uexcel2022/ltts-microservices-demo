package com.uexcel.gatewayserver;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.text.SimpleDateFormat;
import java.time.Duration;
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
							.setFallbackUri("forward:/contactSupport"))
					)
						.uri("lb://CUSTOMER"))

				.route(p->p.path("/saferide/bus/**")
						.filters(f->f.rewritePath("/saferide/bus/(?<segment>.*)","/${segment}")
								.circuitBreaker(config -> config.setName("busCircuitBreaker")
										.setFallbackUri("forward:/contactSupport"))
						)
						.uri("lb://BUS"))

				.route(p->p.path("/saferide/ticketing/**")
						.filters(f->f.rewritePath("/saferide/ticketing/(?<segment>.*)","/${segment}")
								.circuitBreaker(config -> config.setName("ticketingCircuitBreaker")
										.setFallbackUri("forward:/contactSupport"))
						)
						.uri("lb://TICKETING"))

				.build();

	}

	@Bean
	public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer(){
		return factory -> factory.configureDefault(id-> new Resilience4JConfigBuilder(id)
				.circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
				.timeLimiterConfig(TimeLimiterConfig.custom()
						.timeoutDuration(Duration.ofSeconds(4)).build()).build());
	}

	private String getTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSZ");
		return sdf.format(new Date());
	}
}
