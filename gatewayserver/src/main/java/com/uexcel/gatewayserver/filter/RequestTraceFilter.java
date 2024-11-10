package com.uexcel.gatewayserver.filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;


@Component

public class RequestTraceFilter implements GlobalFilter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private final FilterUtility filterUtility;

    public RequestTraceFilter(FilterUtility filterUtility) {
        this.filterUtility = filterUtility;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
        if (filterUtility.getCorrelationId(requestHeaders) != null) {
            logger.debug("saferide-correlation-id found: {}", filterUtility.getCorrelationId(requestHeaders));
        } else {
            String correlationId = UUID.randomUUID().toString();
            exchange = filterUtility.setCorrelationId(exchange, correlationId);
            logger.debug("saferide-correlation-id generated in requestTraceFilter: {}", correlationId);
        }

        return chain.filter(exchange);
    }
}
