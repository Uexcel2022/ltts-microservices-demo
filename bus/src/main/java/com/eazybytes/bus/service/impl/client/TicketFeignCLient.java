package com.eazybytes.bus.service.impl.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("ticket")
public interface TicketFeignCLient {

}
