package com.uexcel.customer.info;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "customer")
@Getter @Setter
public class AppBuiltInfo {
    private String message;
    private Map<String, String> contactDetails;
    private List<String> call;
}
