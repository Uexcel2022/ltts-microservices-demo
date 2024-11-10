package com.uexcel.gatewayserver.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {
    @RequestMapping("/contactSupport")
    public Mono<String> fallback() {
        return Mono.justOrEmpty(
                "We encountered an error. " +
                        "Please retry in few minutes time or contact the support team.");
    }
}
