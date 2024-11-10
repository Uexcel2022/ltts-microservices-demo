package com.uexcel.gatewayserver.filter;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
@Component
public class FilterUtility {
    public static final String CORRELATION_ID = "saferideCorrelationId";

    public String getCorrelationId(HttpHeaders requestHeaders) {
        if(requestHeaders.get(CORRELATION_ID)!= null){
            return requestHeaders.get(CORRELATION_ID).stream().findFirst().get();
        }
        return null;
    }

    public ServerWebExchange setRequestHeaders(ServerWebExchange exchange, String idName, String idValue) {
        return exchange.mutate().request(exchange.getRequest().mutate().header(idName,idValue).build()).build();
    }

    public ServerWebExchange setCorrelationId(ServerWebExchange exchange, String correlationId) {
        return   this.setRequestHeaders(exchange, CORRELATION_ID, correlationId);
    }
}
